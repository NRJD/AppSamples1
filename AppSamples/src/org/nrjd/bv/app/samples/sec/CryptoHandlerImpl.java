/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.sec;


import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class CryptoHandlerImpl implements CryptoHandler {
    private static final String ALGORITHM = "AES";
    private static final int INITIAL_PADDING_SIZE = 100;
    private static final int ALGO_SCHEMA_DATA_SIZE = 100;
    // Aglo schema constants.
    static final int CURRENT_CRYPTO_ALGO_SCHEMA_ID = 1;
    static final int CURRENT_CRYPTO_RELEASE_VER = 1;
    private static final int KEY_SIZE_FOR_SCHEMA_1 = 16;

    public CryptoHandlerImpl() {
    }

    public byte[] encrypt(byte[] data) throws Exception {
        //// Generate key info.
        int keyInfoSize = INITIAL_PADDING_SIZE + ALGO_SCHEMA_DATA_SIZE + KEY_SIZE_FOR_SCHEMA_1;
        byte[] keyInfo = new byte[keyInfoSize];
        // Fill initial padding into key info.
        byte[] initialPadding = KeyGenUtils.generatePadding(INITIAL_PADDING_SIZE);
        KeyGenUtils.copyBytes(initialPadding, keyInfo, 0);
        // Fill algo schema data into key info.
        AlgoSchema algoSchema = new AlgoSchema(CURRENT_CRYPTO_ALGO_SCHEMA_ID, CURRENT_CRYPTO_RELEASE_VER);
        byte[] algoSchemaBytes = AlgoSchema.generateAlgoSchemaBytes(algoSchema, ALGO_SCHEMA_DATA_SIZE);
        KeyGenUtils.copyBytes(algoSchemaBytes, keyInfo, INITIAL_PADDING_SIZE);
        // Add key data
        byte[] keyData = KeyGenUtils.generateKey(KEY_SIZE_FOR_SCHEMA_1);
        KeyGenUtils.copyBytes(keyData, keyInfo, INITIAL_PADDING_SIZE + ALGO_SCHEMA_DATA_SIZE);

        //// Encrypt data.
        Key key = generateKey(keyData);
        Cipher cipher = Cipher.getInstance(getDefaultAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data);

        //// Add key info to the encrypted data.
        byte[] encryptedBytesWithKeyInfo = new byte[keyInfo.length + encryptedBytes.length];
        KeyGenUtils.copyBytes(keyInfo, encryptedBytesWithKeyInfo, 0);
        KeyGenUtils.copyBytes(encryptedBytes, encryptedBytesWithKeyInfo, keyInfoSize);

        //// Encode the data.
        String encodedData = new BASE64Encoder().encode(encryptedBytesWithKeyInfo);
        byte[] encodedBytes = encodedData.getBytes(KeyGenUtils.getCharSet());
        return encodedBytes;
    }

    public byte[] decrypt(byte[] data) throws Exception {
        byte[] decryptedBytes = data;
        try {
            //// Decode the data.
            String encodedData = new String(data, KeyGenUtils.getCharSet());
            byte[] decodedBytes = new BASE64Decoder().decodeBuffer(encodedData);

            //// Process key info.
            // Parse algo schema data from key info.
            byte[] algoSchemaBytes = new byte[ALGO_SCHEMA_DATA_SIZE];
            KeyGenUtils.copyBytes(decodedBytes, INITIAL_PADDING_SIZE, ALGO_SCHEMA_DATA_SIZE, algoSchemaBytes, 0);
            AlgoSchema algoSchema = AlgoSchema.parseAlgoSchemaFromBytes(algoSchemaBytes);
            // If algorithm matches, then try to decrypt the data.
            if ((algoSchema != null) && (algoSchema.getSchemaId() == CURRENT_CRYPTO_ALGO_SCHEMA_ID)) {
                // Get key data
                byte[] keyData = new byte[KEY_SIZE_FOR_SCHEMA_1];
                KeyGenUtils.copyBytes(decodedBytes, INITIAL_PADDING_SIZE + ALGO_SCHEMA_DATA_SIZE, KEY_SIZE_FOR_SCHEMA_1, keyData, 0);

                //// Get encrypted data.
                int keyInfoSize = INITIAL_PADDING_SIZE + ALGO_SCHEMA_DATA_SIZE + KEY_SIZE_FOR_SCHEMA_1;
                int encryptedBytesSize = decodedBytes.length - keyInfoSize;
                byte[] encryptedBytes = new byte[encryptedBytesSize];
                KeyGenUtils.copyBytes(decodedBytes, keyInfoSize, encryptedBytesSize, encryptedBytes, 0);

                //// Decrypt data.
                Key key = generateKey(keyData);
                Cipher cipher = Cipher.getInstance(getDefaultAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, key);
                decryptedBytes = cipher.doFinal(encryptedBytes);
            }
        } catch (Exception e) {
            decryptedBytes = data;
        }
        return decryptedBytes;
    }

    private Key generateKey(byte[] keyValue) throws Exception {
        Key key = new SecretKeySpec(keyValue, getDefaultAlgorithm());
        return key;
    }

    private static String getDefaultAlgorithm() {
        return ALGORITHM;
    }
}
