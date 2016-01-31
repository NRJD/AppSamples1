/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.zip;


public class BookZipTester {
    private static String PATH = "D:\\temp1\\test_data\\AppTestData\\ZipTest\\BookZipTest";
    private static String DEMO_BOOKS_PATH = "D:\\temp1\\test_data\\AppTestData\\ZipTest\\BookZipTest\\DemoBooks";

    private BookZipTester() {
    }

    public static void main(String[] a) {
        testDemoBooksZip();
    }

    private static void testBookZip() {
        testEpubZip(PATH, "B21");
        // testEpubZip(PATH, "S1");
    }

    private static void testDemoBooksZip() {
        String[] bookNames = {};
        // bookNames = new String[] { "BV_Lessons_01_Shraddhavan", "BV_Lessons_02_Sevak", "BV_Lessons_03_Sadhak", "BV_Lessons_04_SP_Ashraya", "BV_Lessons_05_Sri_Guru_Ashraya", "BV_Lessons_06_Diksha" };
        // bookNames = new String[] { "BV_Lessons_01_Shraddhavan", "BV_Lessons_02_Sevak" };
        for (String bookName : bookNames) {
            testZip(DEMO_BOOKS_PATH, bookName + ".epub", bookName + ".bk", "uz_temp_" + bookName, "uz_enc_" + bookName, "uz_dec_" + bookName);
        }
    }

    private static void testEpubZip(String path, String token) {
        testZip(path, token + ".epub", token + ".bk", "uz_temp_" + token, "uz_enc_" + token, "uz_dec_" + token);
    }

    private static void testZip(String path, String zipFileName, String secZipFileName, String tempUnzipFolderName, String encryptedUnzipFolderName,
                                String decryptedUnzipFolderName) {
        // Create zip handlers.
        ZipHandler zip = new ZipHandler(false);
        ZipHandler secZip = new ZipHandler(true);
        // Construct zip file names
        String zipFile = path + "\\" + zipFileName;
        String secZipFile = path + "\\" + secZipFileName;
        // Unzip zip file into temp location.
        String tempUnzipLocation = path + "\\" + tempUnzipFolderName;
        zip.unzip(zipFile, tempUnzipLocation);
        // Secure zip
        secZip.zip(secZipFile, tempUnzipLocation);
        // Unzip encrypted data.
        String encryptedUnzipLocation = path + "\\" + encryptedUnzipFolderName;
        zip.unzip(secZipFile, encryptedUnzipLocation);
        // Unzip decrypted data.
        String decryptedUnzipLocation = path + "\\" + decryptedUnzipFolderName;
        secZip.unzip(secZipFile, decryptedUnzipLocation);
    }
}
