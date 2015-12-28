/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;

import java.util.HashSet;
import java.util.Set;

import org.nrjd.bv.app.util.ArrayUtils;
import org.nrjd.bv.app.util.StringUtils;


/**
 * Crypto utilities.
 */
public class CryptoUtils {
    private static final String META_INF_RESOURCE_URI = "META-INF/".toLowerCase();
    private static final Set<String> EXCLUDE_FILE_NAMES = constructCaseInsensitiveSet("mimetype", "toc.ncx", "book.opf");
    // No need to escape the dot character in the extension names here, because we are
    // using "endsWith" method here, but not the regex patterns.
    private static final Set<String> EXCLUDE_EXTENSIONS = constructCaseInsensitiveSet(".ncx");

    private CryptoUtils() {
    }

    public static boolean isExcludeFromEncryption(String resourcePath) {
        if (StringUtils.isNotNullOrEmpty(resourcePath)) {
            String resourcePathLower = resourcePath.trim().toLowerCase();
            // Check if META-INF resource.
            if (resourcePathLower.indexOf(META_INF_RESOURCE_URI) >= 0) {
                return true;
            }
            // Check exclude file name.
            for (String excludeFileName : EXCLUDE_FILE_NAMES) {
                if (resourcePathLower.endsWith(excludeFileName)) {
                    return true;
                }
            }
            // Check exclude extension.
            for (String excludeExtension : EXCLUDE_EXTENSIONS) {
                if (resourcePathLower.endsWith(excludeExtension)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Set<String> constructCaseInsensitiveSet(String... values) {
        Set<String> caseInsensitiveSet = new HashSet<String>();
        Set<String> set = ArrayUtils.convertToSet(values);
        if (set != null) {
            for (String string : set) {
                if (string != null) {
                    caseInsensitiveSet.add(string.toLowerCase());
                }
            }
        }
        return caseInsensitiveSet;
    }
}
