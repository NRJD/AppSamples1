/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.zip;

import org.nrjd.bv.app.samples.util.CommonUtils;
import org.nrjd.bv.app.samples.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipHandler {
    private static String ESCAPE = "\\";
    private static String ZIP_ENTRY_PATH_SEPARATOR = "/";
    private static String FILE_SEPARATOR = File.separator;
    private static String FILE_SEPARATOR_ESCAPED = getEscapedFileSeparator();
    private static final int BUFFER_SIZE = 4096;
    private boolean secure = true;

    public ZipHandler() {
        this(false);
    }

    public ZipHandler(boolean secure) {
        this.secure = secure;
    }

    public void zip(String zipPath, String filesLocation) {
        // TODO: Validate inputs
        zip(zipPath, filesLocation, extractFileList(new File(filesLocation)));
    }

    public void zip(String zipPath, String filesLocation, List<String> files) {
        System.out.println("Generating zip file from: " + filesLocation + ": files: " + files);
        // TODO: Validate inputs
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(new File(zipPath));
            bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos);
            for (String zipEntryName : files) {
                String zipEntryPath = normalizeZipEntryPath(zipEntryName);
                File zipEntryFile = new File(filesLocation, zipEntryPath);
                if (this.secure) {
                    zipSecureEntry(zos, zipEntryName, zipEntryFile);
                } else {
                    zipEntry(zos, zipEntryName, zipEntryFile);
                }
            }
            System.out.println("Completed zipping the file: " + zipPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to zip the file: " + zipPath, e);
        } finally {
            CommonUtils.closeQuietly(zos);
            CommonUtils.closeQuietly(bos);
            CommonUtils.closeQuietly(fos);
        }
    }

    private static void zipEntry(ZipOutputStream zos, String zipEntryName, File zipEntryFile) {
        System.out.println("Zipping the entry: " + zipEntryName);
        // TODO: Validate inputs
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(zipEntryFile);
            ZipEntry zipEntry = new ZipEntry(zipEntryName);
            zos.putNextEntry(zipEntry);
            byte[] data = new byte[BUFFER_SIZE];
            int readSize = fis.read(data);
            while (readSize != -1) {
                zos.write(data, 0, readSize);
                readSize = fis.read(data);
            }
            zos.closeEntry();
            zos.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to zip entry: " + zipEntryName, e);
        } finally {
            CommonUtils.closeQuietly(fis);
        }
    }

    private static void zipSecureEntry(ZipOutputStream zos, String zipEntryName, File zipEntryFile) {
        System.out.println("Zipping the entry: " + zipEntryName);
        // TODO: Validate inputs
        try {
            byte[] encryptedData = ZipSecurityUtils.getEncryptedZipEntryData(zipEntryName, zipEntryFile);
            ZipEntry zipEntry = new ZipEntry(zipEntryName);
            zos.putNextEntry(zipEntry);
            zos.write(encryptedData);
            zos.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to zip secure entry: " + zipEntryName, e);
        }
    }

    public void unzip(String zipPath, String unzipLocation) {
        System.out.println("Unzipping " + zipPath + ", into: " + unzipLocation);
        // TODO: Validate inputs
        File unzipFolder = new File(unzipLocation);
        if (!unzipFolder.exists()) {
            unzipFolder.mkdir();
        }
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ZipInputStream zis = null;
        try {
            fis = new FileInputStream(zipPath);
            bis = new BufferedInputStream(fis);
            zis = new ZipInputStream(bis);
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                String zipEntryName = entry.getName();
                String zipEntryPath = normalizeZipEntryPath(zipEntryName);
                String filePath = unzipLocation + FILE_SEPARATOR + zipEntryPath;
                if (!entry.isDirectory()) {
                    if (this.secure) {
                        unzipSecureEntry(zis, zipEntryName, filePath);
                    } else {
                        unzipEntry(zis, zipEntryName, filePath);
                    }
                } else {
                    File folder = new File(filePath);
                    folder.mkdir();
                }
                zis.closeEntry();
                entry = zis.getNextEntry();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to unzip the file: " + zipPath, e);
        } finally {
            CommonUtils.closeQuietly(zis);
            CommonUtils.closeQuietly(bis);
            CommonUtils.closeQuietly(fis);
        }
    }

    private static void unzipEntry(ZipInputStream zis, String zipEntryName, String filePath) {
        System.out.println("Unzipping the entry: " + zipEntryName);
        // TODO: Validate inputs
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(filePath);
            createParentDirectoryIfNotExists(file);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] data = new byte[BUFFER_SIZE];
            int readSize = zis.read(data);
            while (readSize != -1) {
                bos.write(data, 0, readSize);
                readSize = zis.read(data);
            }
            bos.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to unzip entry: " + filePath, e);
        } finally {
            CommonUtils.closeQuietly(bos);
            CommonUtils.closeQuietly(fos);
        }
    }

    private static void unzipSecureEntry(ZipInputStream zis, String zipEntryName, String filePath) {
        System.out.println("Unzipping the secure entry: " + zipEntryName);
        // TODO: Validate inputs
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(filePath);
            createParentDirectoryIfNotExists(file);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] decryptedData = ZipSecurityUtils.getDecryptedZipEntryData(zipEntryName, zis);
            bos.write(decryptedData);
            bos.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to unzip entry: " + filePath, e);
        } finally {
            CommonUtils.closeQuietly(bos);
            CommonUtils.closeQuietly(fos);
        }
    }

    private static void createParentDirectoryIfNotExists(File file) {
        // TODO: Validate inputs
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    private static List<String> extractFileList(File folder) {
        // TODO: Validate inputs
        List<String> fileList = new ArrayList<String>();
        String[] subFiles = folder.list();
        for (String subFileName : subFiles) {
            extractFileList(fileList, null, new File(folder, subFileName));
        }
        return fileList;
    }

    private static void extractFileList(List<String> fileList, String filePrefix, File file) {
        // TODO: Validate inputs
        if (file.isFile()) {
            fileList.add(getZipEntryPath(filePrefix, file.getName()));
        } else if (file.isDirectory()) {
            String dirPrefix = getZipEntryPath(filePrefix, file.getName());
            String[] subFiles = file.list();
            for (String subFileName : subFiles) {
                extractFileList(fileList, dirPrefix, new File(file, subFileName));
            }
        }
    }

    private static String getZipEntryPath(String folderPath, String fileName) {
        return (StringUtils.isNotNullOrEmpty(folderPath) ? folderPath + ZIP_ENTRY_PATH_SEPARATOR + fileName : fileName);
    }

    private static String getEscapedFileSeparator() {
        if (FILE_SEPARATOR.equals(ESCAPE)) {
            return ESCAPE + ESCAPE;
        } else {
            return FILE_SEPARATOR;
        }
    }

    private static String normalizeZipEntryPath(String path) {
        if (StringUtils.isNotNullOrEmpty(path)) {
            path = path.replaceAll(ZIP_ENTRY_PATH_SEPARATOR, FILE_SEPARATOR_ESCAPED);
        }
        return path;
    }
}
