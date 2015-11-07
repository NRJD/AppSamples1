/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples;

import java.util.logging.Level;


public class Logger {
    private java.util.logging.Logger logger = null;

    public Logger() {
        this.logger = java.util.logging.Logger.getAnonymousLogger();
    }

    public Logger(String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }

    public void debug(String msg) {
        prn("DEBUG: " + msg);
        this.logger.finest(msg);
    }

    public void info(String msg) {
        prn("INFO: " + msg);
        this.logger.info(msg);
    }

    public void info(String msg, String obj) {
        prn("INFO: " + msg + ": " + obj);
        this.logger.log(Level.INFO, msg, obj);
    }

    public void warn(String msg, String obj) {
        prn("WARNING: " + msg + ": " + obj);
        this.logger.log(Level.WARNING, msg, obj);
    }

    public void error(String msg) {
        prn("SEVERE: " + msg);
        this.logger.severe(msg);
    }

    public void error(String msg, Exception e) {
        prn("SEVERE: " + msg, e);
        this.logger.log(Level.SEVERE, msg, e);
    }

    private static void prn(String msg) {
        prn(msg, null);
    }

    private static void prn(String msg, Exception e) {
        System.out.println(msg);
        if (e != null) {
            e.printStackTrace();
        }
    }
}
