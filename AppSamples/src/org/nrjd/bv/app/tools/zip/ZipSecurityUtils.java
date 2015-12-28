/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.zip.ZipInputStream;

import org.nrjd.bv.app.tools.crypto.CryptoHandler;
import org.nrjd.bv.app.tools.crypto.CryptoHandlerFactory;
import org.nrjd.bv.app.tools.crypto.CryptoUtils;
import org.nrjd.bv.app.tools.util.CommonUtils;


public class ZipSecurityUtils {
    private static final int BUFFER_SIZE = 4096;

    public static byte[] getEncryptedZipEntryData(String zipEntryName, File zipEntryFile) {
        // TODO: Validate inputs
        byte[] encryptedData = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(zipEntryFile);
            int initialBufferSize = ((int)zipEntryFile.length() + 50);
            encryptedData = getEncryptedZipEntryData(zipEntryName, fis, initialBufferSize);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt zip entry data: " + zipEntryName, e);
        } finally {
            CommonUtils.closeQuietly(fis);
        }
        return encryptedData;
    }

    public static byte[] getEncryptedZipEntryData(String zipEntryName, InputStream is, int initialBufferSize) {
        // TODO: Validate inputs
        byte[] encryptedData = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = ((initialBufferSize > 0) ? new ByteArrayOutputStream(initialBufferSize) : new ByteArrayOutputStream());
            byte[] data = new byte[BUFFER_SIZE];
            int readSize = is.read(data);
            while (readSize != -1) {
                baos.write(data, 0, readSize);
                readSize = is.read(data);
            }
            baos.flush();
            byte[] dataBytes = baos.toByteArray();
            if (CryptoUtils.isExcludeFromEncryption(zipEntryName)) {
                encryptedData = dataBytes;
            } else {
                CryptoHandler cryptoHandler = getCryptoHandler();
                encryptedData = cryptoHandler.encrypt(dataBytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt zip entry data: " + zipEntryName, e);
        } finally {
            CommonUtils.closeQuietly(baos);
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
            CryptoHandler cryptoHandler = getCryptoHandler();
            decryptedData = cryptoHandler.decrypt(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt zip entry data: " + zipEntryName, e);
        } finally {
            CommonUtils.closeQuietly(baos);
        }
        return decryptedData;
    }

    private static CryptoHandler getCryptoHandler() {
        return CryptoHandlerFactory.getInstance();
    }
}
