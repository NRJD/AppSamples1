/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.zip;

import java.util.Arrays;
import java.util.List;

public class BookZipTester {
    private static String PATH = "D:\\temp1\\test_data\\AppTestData\\ZipTest\\BookZipTest";

    private BookZipTester() {
    }

    public static void main(String[] a) {
        testEpubZip("B21");
        // testEpubZip("S1");
        System.out.println("============");
    }

    private static void testEpubZip(String token) {
       testZip(token + ".epub", token + ".bk", "uz_temp_" + token, "uz_enc_" + token, "uz_dec_" + token);
    }
    
    private static void testZip(String zipFileName, String secZipFileName, String tempUnzipFolderName, String encryptedUnzipFolderName, String decryptedUnzipFolderName) {
        // Create zip handlers.
        ZipHandler zip = new ZipHandler(false);
        ZipHandler secZip = new ZipHandler(true);
        // Construct zip file names
        String zipFile = PATH + "\\" + zipFileName;
        String secZipFile = PATH + "\\" + secZipFileName;
        // Unzip zip file into temp location.
        String tempUnzipLocation = PATH + "\\" + tempUnzipFolderName;
        zip.unzip(zipFile, tempUnzipLocation);
        // Secure zip
        secZip.zip(secZipFile, tempUnzipLocation);
        // Unzip encrypted data.
        String encryptedUnzipLocation = PATH + "\\" + encryptedUnzipFolderName;
        zip.unzip(secZipFile, encryptedUnzipLocation);
        // Unzip decrypted data.
        String decryptedUnzipLocation = PATH + "\\" + decryptedUnzipFolderName;
        secZip.unzip(secZipFile, decryptedUnzipLocation);
    }
}
