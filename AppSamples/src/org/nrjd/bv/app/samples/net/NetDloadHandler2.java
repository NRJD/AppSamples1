/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.nio.charset.Charset;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import org.nrjd.bv.app.samples.Logger;
import org.nrjd.bv.app.samples.LoggerFactory;


public class NetDloadHandler2 {
    private static final Logger LOG = LoggerFactory.getLogger();
    private HttpClient httpClient = null;
    private File destFile = null;
    private Exception failure = null;

    public NetDloadHandler2() {
        // HttpClient client = AndroidHttpClient.newInstance("SampleApp");
        this.httpClient = wrapClient(HttpClients.createDefault());
    }

    public File downloadFile(String url, String downloadDir) {
        // TODO: Validations
        // TODO: Use Jedi for options
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
                if (!fileName.endsWith(".epub")) {
                    fileName = fileName + ".epub";
                }

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
                long lenghtOfFile = response.getEntity().getContentLength();

                // this is where the file will be seen after the download
                FileOutputStream f = new FileOutputStream(destFile);

                try {
                    // file input is from the url
                    InputStream in = response.getEntity().getContent();

                    // here's the download code
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    long total = 0;

                    while ((len1 = in.read(buffer)) > 0 && !isCancelled()) {
                        // Make sure the user can cancel the download.
                        if (isCancelled()) {
                            return null;
                        }
                        total += len1;
                        publishProgress(total, lenghtOfFile, (long)((total * 100) / lenghtOfFile));
                        f.write(buffer, 0, len1);
                    }
                } finally {
                    f.close();
                }

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

        return null;
    }

    private boolean isCancelled() {
        return false;
    }

    private void publishProgress(long total, long lenghtOfFile, long progress) {
        LOG.info("Downloaded: Total: " + total + ", lenghtOfFile: " + lenghtOfFile + ", progress: " + progress);
    }
    
    public static HttpClient wrapClient(HttpClient base) {
    try {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = base.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", ssf, 443));
        return new DefaultHttpClient(ccm, base.getParams());
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
    }
}
