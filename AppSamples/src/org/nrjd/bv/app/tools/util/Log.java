/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.FileInputStream;

import java.io.InputStreamReader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;


public class Log {
    public boolean isDebugEnabled() {
        return true;
    }
    
    public void debug(String msg) {
        System.out.println(msg);
    }

    public void warn(String msg) {
        System.out.println(msg);
    }
    
    public void error(String msg) {
        System.out.println(msg);
    }
}
