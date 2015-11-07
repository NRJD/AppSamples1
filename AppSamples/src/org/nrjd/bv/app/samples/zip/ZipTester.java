/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.zip;

import java.util.Arrays;
import java.util.List;

public class ZipTester {
    private static String PATH = "D:\\temp1\\test_data\\AppTestData\\ZipTest\\DataZipTest";

    private ZipTester() {
    }

    public static void main(String[] a) {
        testZip(false);
        testUnzip(false);
        System.out.println("-------------");
        testZip(true);
        testUnzip(true);
    }

    private static void testZip(boolean security) {
        ZipHandler zip = new ZipHandler(security);
        String encStr = (security ? "-enc" : "");
        // Zip1
        String zipFile = PATH + "\\data1" + encStr + ".zip";
        String filesLocation = PATH + "\\zip1";
        List<String> files = Arrays.<String>asList("test1.txt", "test2.txt", "11/test3.txt");
        zip.zip(zipFile, filesLocation, files);
        // Zip2
        zipFile = PATH + "\\data2" + encStr + ".zip";
        filesLocation = PATH + "\\zip2";
        zip.zip(zipFile, filesLocation);
    }

    private static void testUnzip(boolean security) {
        ZipHandler zip = new ZipHandler(security);
        String encStr = (security ? "-enc" : "");
        // Zip1
        String zipFile = PATH + "\\data1" + encStr + ".zip";
        String unzipLocation = PATH + "\\UnzipTest1" + encStr;
        zip.unzip(zipFile, unzipLocation);
        // Zip2
        zipFile = PATH + "\\data2" + encStr + ".zip";
        unzipLocation = PATH + "\\UnzipTest2" + encStr;
        zip.unzip(zipFile, unzipLocation);
    }
}
