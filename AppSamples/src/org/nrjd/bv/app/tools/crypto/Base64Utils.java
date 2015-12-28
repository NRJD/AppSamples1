/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * Internal Base64 utilities.
 */
class Base64Utils {
    private Base64Utils() {
    }

    public static byte[] encode(byte[] data) {
        byte[] encodedBytes = null;
        try {
            String encodedData = new BASE64Encoder().encode(data);
            if (encodedData != null) {
                encodedBytes = encodedData.getBytes(KeyGenUtils.getCharSet());
            }
        } catch (Exception e) {
            encodedBytes = null;
        }
        return encodedBytes;
    }

    public static byte[] decode(byte[] data) throws Exception {
        byte[] decodedBytes = null;
        try {
            String encodedData = new String(data, KeyGenUtils.getCharSet());
            decodedBytes = new BASE64Decoder().decodeBuffer(encodedData);
        } catch (Exception e) {
            decodedBytes = null;
        }
        return decodedBytes;
    }
}
