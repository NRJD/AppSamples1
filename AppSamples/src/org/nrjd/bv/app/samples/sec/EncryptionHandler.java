/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.sec;


import org.nrjd.bv.app.samples.AppConstants;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class EncryptionHandler {
    private static final String ALGORITHM = "AES";
    private static final String PSWD_HASH_ALGORITHM = "SHA-256";
    // Don't store key in String.
    private static final byte[] DEFAULT_KEY = new byte[] { 'e', 'n', 'c', 'r', 'y', 'p', 't', 'i', 'n', 'g', 't', 'h', 'e', 'b', 'v', 'd', 'a', 't', 'a' };
    private static final String CHARSET = AppConstants.UTF8;
    private byte[] keyData = null;

    public EncryptionHandler() {
        this(null);
    }

    public EncryptionHandler(byte[] key) {
        this.keyData = key;
    }

    public byte[] encrypt(byte[] data) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(getDefaultAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data);
        String encodedData = new BASE64Encoder().encode(encryptedBytes);
        byte[] encodedBytes = encodedData.getBytes(CHARSET);
        return encodedBytes;
    }

    public byte[] decrypt(byte[] data) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(getDefaultAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        String encryptedData = new String(data, CHARSET);
        byte[] decodedBytes = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return decryptedBytes;
    }

    private Key generateKey() throws Exception {
        return generateKey16();
        // return generateSHAKey();
    }

    private Key generateKey16() throws Exception {
        byte[] keyBytes = (((keyData != null) && (keyData.length > 0)) ? keyData : DEFAULT_KEY);
        byte[] key16 = new byte[16];
        for (int index = 0; index < key16.length; index++) {
            if ((keyBytes != null) && (keyBytes.length > index)) {
                key16[index] = keyBytes[index];
            } else {
                key16[index] = (byte)('A' + index % 26);
            }
        }
        Key key = new SecretKeySpec(key16, getDefaultAlgorithm());
        return key;
    }

    private Key generateSHAKey() throws Exception {
        byte[] keyBytes = (((keyData != null) && (keyData.length > 0)) ? keyData : DEFAULT_KEY);
        MessageDigest messageDigest = MessageDigest.getInstance(PSWD_HASH_ALGORITHM);
        messageDigest.update(keyBytes);
        byte[] secretKeyData = messageDigest.digest();
        Key key = new SecretKeySpec(secretKeyData, getDefaultAlgorithm());
        return key;
    }

    private static String getDefaultAlgorithm() {
        return ALGORITHM;
    }
}
