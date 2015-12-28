/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.util;


public class StringUtils {
    public static boolean isNullOrEmpty(String string) {
        return ((string == null) || string.trim().equals(""));
    }

    public static boolean isNotNullOrEmpty(String string) {
        return (!isNullOrEmpty(string));
    }

    public static byte[] getBytes(String data, String charsetName) {
        try {
            return data.getBytes(charsetName);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting the data to bytes", e);
        }
    }
}
