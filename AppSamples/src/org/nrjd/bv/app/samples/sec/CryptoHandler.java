/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.sec;


public interface CryptoHandler {
    public byte[] encrypt(byte[] data) throws Exception;

    public byte[] decrypt(byte[] data) throws Exception;
}
