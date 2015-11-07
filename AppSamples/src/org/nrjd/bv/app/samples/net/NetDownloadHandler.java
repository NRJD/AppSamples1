/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.net;

import org.nrjd.bv.app.samples.Logger;
import org.nrjd.bv.app.samples.LoggerFactory;
import org.nrjd.bv.app.samples.reg.BookEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.net.URLEncoder;

import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;


/**
 * Net download handler.
 */
public class NetDownloadHandler {
    private static final Logger LOG = LoggerFactory.getLogger();
    private HttpClient httpClient = null;
    private List<String> summary = new ArrayList<String>();

    public NetDownloadHandler() {
        // this.httpClient = new NetDownloadHelpers.SSLHttpClient(new BasicHttpParams());
        this.httpClient = NetDloadHandler3.getNewHttpClient();

    }

    public void printSummarry() {
        LOG.debug("*** Summary ***");
        LOG.debug(NetDownloadUtils.toString(summary, "\n", true));
    }

    public File downloadFile(String url, String downloadDir) {
        return downloadFile(url, downloadDir, null);
    }

    public File downloadFile(String url, String downloadDir, BookEntry bookEntry) {
        // TODO: Validations
        // TODO: Use Jedi for options
        File destFile = null;
        Exception failure = null;
        try {
            LOG.debug("Downloading: " + url);

            String fileName = url.substring(url.lastIndexOf('/') + 1);
            fileName = fileName.replaceAll("\\?|&|=", "_");

            if (fileName.trim().equals("")) {
                summary.add("Error: " + destFile + ": File Name couldn't be found");
                fileName = "12345";
            }

            HttpGet get = new HttpGet(url);
            get.setHeader("User-Agent", NetDownloadUtils.getUserAgent());
            get.setHeader("Content-Length", "0");
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


                // Content-Length: 899405
                Header[] headers = response.getAllHeaders();
                if (headers != null) {
                    for (Header h : headers) {
                        LOG.debug("H: " + h);
                    }
                }

                // lenghtOfFile is used for calculating download progress
                LOG.debug("response.getEntity().getContentType(): " + response.getEntity().getContentType());
                LOG.debug("response.getEntity().getContentLength(): " + response.getEntity().getContentLength());
                LOG.debug("response.getEntity().getContentEncoding(): " + response.getEntity().getContentEncoding());

                downloadBinaryData(response, destFile, bookEntry);
            } else {
                failure = new RuntimeException(response.getStatusLine().getReasonPhrase());
                LOG.error("Download failed: " + response.getStatusLine().getReasonPhrase());
            }

        } catch (Exception e) {
            LOG.error("Download failed.", e);
            failure = e;
        }
        if (failure != null) {
            summary.add("Error: " + destFile + ": " + failure.getMessage());
        }
        return destFile;
    }

    private void downloadBinaryData(HttpResponse response, File destFile, BookEntry bookEntry) throws Exception {
        long total = 0;
        long lenghtOfFile = response.getEntity().getContentLength();
        if((lenghtOfFile < 0) && (bookEntry != null)) {
            LOG.debug("Got file length: " + lenghtOfFile + ": Assigning from registry: " + bookEntry.getSize());
            lenghtOfFile = bookEntry.getSize();
        }
        Header type = response.getEntity().getContentType();
        // this is where the file will be seen after the download
        FileOutputStream f = new FileOutputStream(destFile);
        try {
            // file input is from the url
            InputStream in = response.getEntity().getContent();
            // here's the download code
            byte[] buffer = new byte[4096];
            int len1 = 0;

            while ((len1 = in.read(buffer)) > 0 && !isCancelled()) {
                // Make sure the user can cancel the download.
                if (isCancelled()) {
                    return;
                }
                total += len1;
                publishProgress(total, lenghtOfFile, (long)((total * 100) / lenghtOfFile));
                f.write(buffer, 0, len1);
            }

            if (!isCancelled()) {
                //                    //FIXME: This doesn't belong here really...
                //                    Book book = new EpubReader().readEpubLazy(destFile.getAbsolutePath(), "UTF-8");
                //                    libraryService.storeBook(destFile.getAbsolutePath(), book, false, config.getCopyToLibraryOnScan());
            }
        } finally {
            f.close();
        }
        // TODO: Verify original vs actual
        summary.add("Downloaded: " + destFile + ": type: " + type + ": " + total + " bytes");
    }

    private boolean isCancelled() {
        return false;
    }

    private void publishProgress(long total, long lenghtOfFile, long progress) {
        LOG.debug("Downloaded: Total: " + total + ", lenghtOfFile: " + lenghtOfFile + ", progress: " + progress);
    }
}
