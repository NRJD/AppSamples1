/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.net;

import org.nrjd.bv.app.samples.util.FileUtils;

import org.nrjd.bv.app.samples.reg.BookEntry;
import org.nrjd.bv.app.samples.reg.RegistryData;
import org.nrjd.bv.app.samples.reg.RegistryDataUtils;

import java.io.File;

import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NetDownloadTest {
    // TestData2
    static final String NET_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lZ0dPNURjODdBSVk";
    static final List<String> BOOK_NAMES = Arrays.<String>asList("I1.epub", "I2.epub", "I3.epub", "B1.epub");
    static final List<String> BOOK_NAMES_LARGE = concatList(BOOK_NAMES, /* Large file 28MB */Arrays.<String>asList("R1.epub"));
    static final List<String> BOOK_NAMES_ALL = concatList(BOOK_NAMES);
    static final List<String> BOOK_NAMES_ALL_LARGE = concatList(BOOK_NAMES, BOOK_NAMES_LARGE);

    // TestData1
    private static String NET_FOLDER_21 = "https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems";
    private static final List<String> BOOK_NAMES_21 =
        Arrays.<String>asList("A1.epub", "G1.epub", "M1.epub", "database.txt", "java.txt", "LordDamodar1.jpg", "LordDamodar2.jpg",
                              "hdg_srila_prabhupada_japa_clip1.mp3", "hh_jayapataka_swami_japa_clip1.mp3", "sheet1.xlsx", "sheet2.xlsx", "SamplePdf1.pdf",
                              "SamplePdf2.pdf");

    static List<String> concatList(List<String>... listArray) {
        List<String> newList = new ArrayList<String>();
        if (listArray != null) {
            for (List<String> list : listArray) {
                newList.addAll(list);
            }
        }
        return newList;
    }

    static void addBookUrls(Set<String> urls, String urlPrefix, List<String> bookNames) {
        for (String bookName : bookNames) {
            urls.add(urlPrefix + "/" + URLEncoder.encode(bookName));
        }
    }

    static void addData1BookUrls(Set<String> urls) {
        urls.add("https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems/sample1.mp3");
        urls.add(NET_FOLDER_21);
        addBookUrls(urls, NET_FOLDER_21, BOOK_NAMES_21);
    }

    /**
     * Test Program.
     * @param args args.
     */
    public static void main(String[] args) throws Exception {
        // download2();
        download1();
    }

    static void download1() {
        download1(NET_FOLDER, "registry1.xml");
    }

    static void download1(String url, String registryFileName) {
        String downloadDir = "D:\\temp1\\test_data\\AppTestData\\NetTest";
        String registryUrl = url + "/" + registryFileName;
        prn("downloadDir: " + downloadDir);
        prn("registryUrl: " + registryUrl);
        String registryFileData = null;
        RegistryData registryData = null;
        NetDownloadHandler netDownloadHandler = new NetDownloadHandler();
        try {
            File registryFile = netDownloadHandler.downloadFile(registryUrl, downloadDir);
            registryFileData = FileUtils.getFileData(registryFile);
            prn("registryFileData:\n" +
                    registryFileData);
        } catch (Exception e) {
            prn("Error occurred while downloading the registry data: " + e.getMessage());
            e.printStackTrace(System.out);
            return;
        }
        try {
            registryData = RegistryDataUtils.parseRegistryXml(registryFileData);
        } catch (Exception e) {
            prn("Error occurred while parsing the registry data: " + e.getMessage());
            e.printStackTrace(System.out);
            return;
        }
        if (registryData == null) {
            prn("ERROR: No registry data available!");
            return;
        }
        List<BookEntry> bookEntries = registryData.getBookEntries();
        if ((bookEntries == null) || (bookEntries.size() < 1)) {
            prn("ERROR: No book entries available!");
            return;
        }
        prn("registryData:\n" +
                registryData);
        // Download files
        for (BookEntry bookEntry : bookEntries) {
            String bookUrl = url + "/" + bookEntry.getFileName();
            prn("Downloading Book: " + bookUrl);
            try {
                netDownloadHandler.downloadFile(bookUrl, downloadDir, bookEntry);
            } catch (Exception e) {
                prn("Error occurred while downloading the file: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
        netDownloadHandler.printSummarry();
    }

    static void download2() {
        Set<String> urls = new HashSet<String>();
        // addData1BookUrls(urls);
        // addBookUrls(urls, NET_FOLDER_21, BOOK_NAMES_21);
        addBookUrls(urls, NET_FOLDER, BOOK_NAMES_ALL);
        // addBookUrls(urls, NET_FOLDER, BOOK_NAMES);
        String downloadDir = "D:\\temp1\\test_data\\AppTestData\\NetTest";
        prn("downloadDir: " + downloadDir);
        // NetDloadHandler3 netDownloadHandler = new NetDloadHandler3();
        // HttpsUrlHandler netDownloadHandler = new HttpsUrlHandler();
        NetDownloadHandler netDownloadHandler = new NetDownloadHandler();
        for (String url : urls) {
            prn("Downloading URL: " + url);
            try {
                netDownloadHandler.downloadFile(url, downloadDir);
            } catch (Exception e) {
                prn("Error occurred while downloading the file: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
        netDownloadHandler.printSummarry();
    }

    private static void prn(String msg) {
        System.out.println(msg);
    }
}
