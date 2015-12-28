/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.util;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileInputStream;

import java.io.InputStreamReader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;


public class FileUtils {
    /**
     * Retrives data from the given file.
     * @param file file to be read.
     * @return data retrieved from the given file.
     */
    public static String getFileData(File file) {
        prn("Reading data from file: " + file + "..");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder buffer = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            char[] readBuffer = new char[4098];
            int length = 0;
            while ((length = br.read(readBuffer)) > 0) {
                buffer.append(readBuffer, 0 , length);
            }

        } catch (Exception e) {
            prn("Failed to read data from: " + file + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e = null; /* Ignore error */
            }
            try {
                isr.close();
            } catch (Exception e) {
                e = null; /* Ignore error */
            }
            try {
                fis.close();
            } catch (Exception e) {
                e = null; /* Ignore error */
            }
        }
        return buffer.toString();
    }

    private static void prn(String msg) {
        System.out.println(msg);
    }
}
