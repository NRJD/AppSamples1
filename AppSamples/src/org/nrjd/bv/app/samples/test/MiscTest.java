/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.test;

import java.net.URLEncoder;

import java.util.HashSet;
import java.util.Set;


public class MiscTest {

    /**
     * Test Program.
     * @param args args.
     */
    public static void main(String[] args) throws Exception {
        testURLEncoding();
    }

    private static void testURLEncoding() throws Exception {
        String[] inputs = { "A_Z", "0-9", "_-.", "~`!@#$%^&*()_-+=|\\{}[]:;\"'<>,.?/" };
        for(String input : inputs) {
            prn("URLEncode(" + input + "): " + URLEncoder.encode(input, "UTF-8"));
        }
    }

    private static void prn(String msg) {
        System.out.println(msg);
    }
}
