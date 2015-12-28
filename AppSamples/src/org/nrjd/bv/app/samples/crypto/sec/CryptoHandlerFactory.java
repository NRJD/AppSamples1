/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.crypto.sec;


public class CryptoHandlerFactory {

    private CryptoHandlerFactory() {
    }

    public static CryptoHandler getInstance() {
        // return new BasicCryptoHandler();
        return new CryptoHandlerImpl();
    }
}
