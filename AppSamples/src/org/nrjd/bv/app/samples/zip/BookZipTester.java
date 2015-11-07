/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.zip;

import java.util.Arrays;
import java.util.List;

public class BookZipTester {
    private static String PATH = "D:\\temp1\\test_data\\AppTestData\\ZipTest\\BookZipTest";

    private BookZipTester() {
    }

    public static void main(String[] a) {
        testEpubZip("S1");
        System.out.println("============");
    }

    private static void testEpubZip(String token) {
       testZip(token + ".epub", token + ".sepub", "uz_temp_" + token, "uz_enc_" + token, "uz_" + token);
    }
    
    private static void testZip(String zipFileName, String secZipFileName, String tempUnzipFolderName, String unzipFolderName, String secUnzipFolderName) {
        ZipHandler zip = new ZipHandler(false);
        String zipFile = PATH + "\\" + zipFileName;
        String unzipLocation = PATH + "\\" + tempUnzipFolderName;
        zip.unzip(zipFile, unzipLocation);
        // Secure zip
        ZipHandler secZip = new ZipHandler(true);
        String secZipFile = PATH + "\\" + secZipFileName;
        secZip.zip(secZipFile, unzipLocation);
        // unzip
        unzipLocation = PATH + "\\" + unzipFolderName;
        zip.unzip(secZipFile, unzipLocation);
        // Secure unzip
        unzipLocation = PATH + "\\" + secUnzipFolderName;
        secZip.unzip(secZipFile, unzipLocation);
    }
}
