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

    public static String encode(byte[] data) {
        String encodedData = new BASE64Encoder().encode(data);
        return encodedData;
    }

    public static byte[] decode(java.lang.String data) throws Exception {
        byte[] decodedBytes = new BASE64Decoder().decodeBuffer(data);
        return decodedBytes;
    }
}
