/*
 * Copyright (C) 2013 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.net.backup;

import org.nrjd.bv.app.samples.net.NetDloadHandler3;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NetDownloadTest_Working1 {

    /**
     * Test Program.
     * @param args args.
     */
    public static void main(String[] args) {
        Set<String> urls = new HashSet<String>();
        String netFolder = "https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems/";
        urls.add("https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems/sample1.mp3");
        urls.add(netFolder + "A1.epub");
        urls.add(netFolder + "G1.epub");
        urls.add(netFolder + "M1.epub");
        urls.add(netFolder + "java.txt");
       urls.add(netFolder + "database.txt");
        String downloadDir = "D:\\temp1\\test_data\\AppTestData\\NetTest";
        prn("downloadDir: " + downloadDir);
        for (String url : urls) {
            prn("Downloading URL: " + url);
            try {
                NetDloadHandler3 netDownloadHandler = new NetDloadHandler3();
                // HttpsUrlHandler netDownloadHandler = new HttpsUrlHandler();
                File downloadedFile = netDownloadHandler.downloadFile(url, downloadDir);
                prn("downloadedFile: " + downloadedFile);
            } catch (Exception e) {
                prn("Error occurred while downloading the file: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    private static void prn(String msg) {
        System.out.println(msg);
    }
}
