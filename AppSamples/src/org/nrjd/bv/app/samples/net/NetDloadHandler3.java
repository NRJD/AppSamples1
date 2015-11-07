/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import java.io.Writer;

import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;

import java.nio.charset.Charset;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import org.nrjd.bv.app.samples.Logger;
import org.nrjd.bv.app.samples.LoggerFactory;


public class NetDloadHandler3 {
    private static final Logger LOG = LoggerFactory.getLogger();
    private HttpClient httpClient = null;
    private Exception failure = null;

    public NetDloadHandler3() {
        this.httpClient = getNewHttpClient();
    }

    public File downloadFile(String url, String downloadDir) {
        // TODO: Validations
        // TODO: Use Jedi for options
        File destFile = null;
        try {
            LOG.debug("Downloading: " + url);

            String fileName = url.substring(url.lastIndexOf('/') + 1);
            fileName = fileName.replaceAll("\\?|&|=", "_");

            HttpGet get = new HttpGet(url);
            get.setHeader("User-Agent", NetDownloadUtils.getUserAgent());
            HttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                if (NetDownloadUtils.isNullOrEmpty(downloadDir)) {
                    throw new IllegalStateException("Could not get download folder!");
                }

                File downloadFolder = new File(downloadDir);
                if (!downloadFolder.exists()) {
                    downloadFolder.mkdirs();
                }

                /**
                 * Make sure we always store downloaded files as .epub,
                 * so they show up in scans later on.
                 */
                // TODO: Fix for secure .espub
//                if (!fileName.endsWith(".epub")) {
//                    fileName = fileName + ".epub";
//                }
                // Default Charset for android is UTF-8*

                String charsetName = Charset.defaultCharset().name();
                if (!Charset.isSupported(charsetName)) {
                    LOG.warn("{} is not a supported Charset. Will fall back to UTF-8", charsetName);
                    charsetName = "UTF-8";
                }

                try {
                    destFile = new File(downloadFolder, URLDecoder.decode(fileName, charsetName));
                } catch (UnsupportedEncodingException e) {
                    // Won't ever reach here
                    throw new AssertionError(e);
                }

                if (destFile.exists()) {
                    destFile.delete();
                }

                // lenghtOfFile is used for calculating download progress
                LOG.debug("response.getEntity().getContentType(): " + response.getEntity().getContentType());
                LOG.debug("response.getEntity().getContentLength(): " + response.getEntity().getContentLength());
                LOG.debug("response.getEntity().getContentEncoding(): " + response.getEntity().getContentEncoding());

                downloadBinaryData(response, destFile);

                if (!isCancelled()) {
                    //                    //FIXME: This doesn't belong here really...
                    //                    Book book = new EpubReader().readEpubLazy(destFile.getAbsolutePath(), "UTF-8");
                    //                    libraryService.storeBook(destFile.getAbsolutePath(), book, false, config.getCopyToLibraryOnScan());
                }
            } else {
                this.failure = new RuntimeException(response.getStatusLine().getReasonPhrase());
                LOG.error("Download failed: " + response.getStatusLine().getReasonPhrase());
            }

        } catch (Exception e) {
            LOG.error("Download failed.", e);
            this.failure = e;
        }

        return destFile;
    }

    private boolean isCancelled() {
        return false;
    }

    private void publishProgress(long total, long lenghtOfFile, long progress) {
        LOG.info("Downloaded: Total: " + total + ", lenghtOfFile: " + lenghtOfFile + ", progress: " + progress);
    }
    
    private void downloadBinaryData(HttpResponse response, File destFile) throws Exception {
        long lenghtOfFile = response.getEntity().getContentLength();
        // this is where the file will be seen after the download
        FileOutputStream f = new FileOutputStream(destFile);
        try {
            // file input is from the url
            InputStream in = response.getEntity().getContent();
            // here's the download code
            byte[] buffer = new byte[4096];
            int len1 = 0;
            long total = 0;

            while ((len1 = in.read(buffer)) > 0 && !isCancelled()) {
                // Make sure the user can cancel the download.
                if (isCancelled()) {
                    return;
                }
                total += len1;
                publishProgress(total, lenghtOfFile, (long)((total * 100) / lenghtOfFile));
                f.write(buffer, 0, len1);
            }
        } finally {
            f.close();
        }
    }

    private void downloadCharData(HttpResponse response, File destFile) throws Exception {
        long lenghtOfFile = response.getEntity().getContentLength();
        // this is where the file will be seen after the download
        FileOutputStream f = new FileOutputStream(destFile);
        Writer w = new BufferedWriter(new OutputStreamWriter(f, "UTF-8"));

        try {
            // file input is from the url
            InputStream in = response.getEntity().getContent();
            Reader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // here's the download code
            char[] buffer = new char[1024];
            int len1 = 0;
            long total = 0;

            while ((len1 = r.read(buffer)) > 0 && !isCancelled()) {
                // Make sure the user can cancel the download.
                if (isCancelled()) {
                    return;
                }
                total += len1;
                publishProgress(total, lenghtOfFile, (long)((total * 100) / lenghtOfFile));
                w.write(buffer, 0, len1);
            }
        } finally {
            f.close();
        }
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static class MySSLSocketFactory extends SSLSocketFactory {
        private SSLContext sslContext = null;

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);
            this.sslContext = SSLContext.getInstance("TLS");
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            this.sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return this.sslContext.getSocketFactory().createSocket();
        }
    }
}
