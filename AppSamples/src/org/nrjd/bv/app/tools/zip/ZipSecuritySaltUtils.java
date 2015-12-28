/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.zip;

import org.nrjd.bv.app.util.AppConstants;
import org.nrjd.bv.app.util.CommonUtils;

import org.nrjd.bv.app.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Arrays;
import java.util.zip.ZipInputStream;


public class ZipSecuritySaltUtils {
    private static final int BUFFER_SIZE = 4096;
    private static byte[] salt = StringUtils.getBytes("KEY", AppConstants.UTF8);

    public static byte[] getEncryptedZipEntryData(String zipEntryName, File zipEntryFile) {
        // TODO: Validate inputs
        byte[] encryptedData = null;
        ByteArrayOutputStream baos = null;
        FileInputStream fis = null;
        try {
            baos = new ByteArrayOutputStream((int)zipEntryFile.length() + 50);
            baos.write(salt);
            fis = new FileInputStream(zipEntryFile);
            byte[] data = new byte[BUFFER_SIZE];
            int readSize = fis.read(data);
            while (readSize != -1) {
                baos.write(data, 0, readSize);
                readSize = fis.read(data);
            }
            baos.flush();
            encryptedData = baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt zip entry data: " + zipEntryName, e);
        } finally {
            CommonUtils.closeQuietly(baos);
            CommonUtils.closeQuietly(fis);
        }
        return encryptedData;
    }

    public static byte[] getDecryptedZipEntryData(String zipEntryName, ZipInputStream zis) {
        // TODO: Validate inputs
        byte[] decryptedData = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int readSize = zis.read(data);
            while (readSize != -1) {
                baos.write(data, 0, readSize);
                readSize = zis.read(data);
            }
            baos.flush();
            byte[] encryptedData = baos.toByteArray();
            decryptedData = getDecryptedData(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt zip entry data: " + zipEntryName, e);
        } finally {
            CommonUtils.closeQuietly(baos);
        }
        return decryptedData;
    }

    public static byte[] getDecryptedData(byte[] encryptedData) {
        byte[] decryptedData = encryptedData;
        if ((encryptedData != null) && (encryptedData.length >= salt.length)) {
            boolean match = true;
            for (int index = 0; index < salt.length; index++) {
                if (encryptedData[index] != salt[index]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                decryptedData = Arrays.copyOfRange(encryptedData, salt.length, encryptedData.length);
            }
        }
        return decryptedData;
    }
}
