/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;


public class HttpsTest1 {
    private static final String HTTPS_URL = "https://host:8080/";

    public static void main(String[] args) {
        testHttpsConnection();
    }

    private static void testHttpsConnection() {
        try {
            URL url = new URL(HTTPS_URL);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
            printCertificateInfo(httpsURLConnection);
            printUrlContent(httpsURLConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printCertificateInfo(HttpsURLConnection httpsURLConnection) {
        if (httpsURLConnection != null) {
            try {
                prn("Response Code: " + httpsURLConnection.getResponseCode());
                prn("Cipher Suite: " + httpsURLConnection.getCipherSuite());
                prn("\n");
                Certificate[] certificates = httpsURLConnection.getServerCertificates();
                for (Certificate certificate : certificates) {
                    prn("Certificate Type : " + certificate.getType());
                    prn("Certificate Hash Code : " + certificate.hashCode());
                    prn("Certificate Public Key Algorithm : " + certificate.getPublicKey().getAlgorithm());
                    prn("Certificate Public Key Format : " + certificate.getPublicKey().getFormat());
                    prn("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void printUrlContent(HttpsURLConnection httpsURLConnection) throws Exception {
        if (httpsURLConnection != null) {
            BufferedReader br = null;
            try {
                prn("##### URL content");
                br = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String input = br.readLine();
                while (input != null) {
                    prn(input);
                    input = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    br.close();
                }
            }
        }
    }

    private static void prn(String msg) {
        prn(msg);
    }
}
