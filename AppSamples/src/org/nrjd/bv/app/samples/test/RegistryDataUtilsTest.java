/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.test;

import java.nio.charset.Charset;

import java.nio.charset.StandardCharsets;

import org.nrjd.bv.app.samples.reg.BookEntry;
import org.nrjd.bv.app.samples.reg.RegistryData;
import org.nrjd.bv.app.samples.reg.RegistryDataUtils;
import org.nrjd.bv.app.tools.util.DateUtils;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Registy Data utilities.
 */
public class RegistryDataUtilsTest {
    // Registry test data.
    private static final RegistryData REGISTRY_DATA = createReistryData();
    private static final String XML_HEADER = "<?xml version = '1.0' encoding = 'UTF-8'?>\r\n";
    private static final String REGISTRY_XML_WITHOUT_XML_HEADER =
        "<RegistryData><BookEntries><BookEntry><bookName>file 1</bookName><fileName>file1.sepub</fileName><lastModified>2014-11-05 15:05:25</lastModified><size>345</size></BookEntry><BookEntry><bookName>file 2</bookName><fileName>file2.sepub</fileName><lastModified>2015-03-25 10:35:55</lastModified><size>2692</size></BookEntry></BookEntries></RegistryData>";
    private static final String REGISTRY_XML = XML_HEADER + REGISTRY_XML_WITHOUT_XML_HEADER;

    /**
     * Default constructor.
     */
    public RegistryDataUtilsTest() {
    }

    /**
     * Setup to be done before each method.
     * <br>
     * @throws Exception if some error occurs in setup.
     */
    @Before
    public void setUp() throws Exception {
    }

    private static RegistryData createReistryData() {
        List<BookEntry> bookEntries = Arrays.<BookEntry>asList(createReistryEntry("file 1", "file1.sepub", "2014-11-05 15:05:25", 345), // Book1
                createReistryEntry("file 2", "file2.sepub", "2015-03-25 10:35:55", 2692)); // Book2
        return createReistryData(bookEntries);
    }

    private static RegistryData createReistryData(List<BookEntry> bookEntries) {
        RegistryData registryData = new RegistryData();
        registryData.setBookEntries(bookEntries);
        return registryData;
    }

    private static BookEntry createReistryEntry(String bookName, String fileName, String lastModified, long size) {
        BookEntry bookEntry = new BookEntry();
        bookEntry.setBookName(bookName);
        bookEntry.setFileName(fileName);
        bookEntry.setLastModifiedDate(DateUtils.parseDate(lastModified));
        bookEntry.setSize(size);
        return bookEntry;
    }

    /**
     * Positive test for generating registry byte representation by passing valid registry data.
     * @throws Exception if any error occurs while executing the test.
     */
    @Test
    public void testGenerateRegistryData_WithValidRegistryData() throws Exception {
        String actualRegistryXml = RegistryDataUtils.generateRegistryXml(REGISTRY_DATA);
        System.out.println("expectedRegistryXml: " + REGISTRY_XML);
        System.out.println("  actualRegistryXml: " + actualRegistryXml);
        Assert.assertNotNull("The generated registry xml is expected to be not null", actualRegistryXml);
        Assert.assertEquals("Verify the generated registry xml", REGISTRY_XML, actualRegistryXml);
    }

    /**
     * Negative test for generating registry byte representation by passing null registry data.
     * @throws Exception if any error occurs while executing the test.
     */
    @Test
    public void testGenerateRegistryData_WithNullRegistryData() throws Exception {
        String actualRegistryXml = RegistryDataUtils.generateRegistryXml(null);
        Assert.assertNull("The generated registry xml is expected to be null", actualRegistryXml);
    }

    /**
     * Positive test for parsing registry byte representation by passing valid registry byte data.
     * @throws Exception if any error occurs while executing the test.
     */
    @Test
    public void testParseRegistryData_WithValidRegistryData() throws Exception {
        RegistryData registryData = RegistryDataUtils.parseRegistryXml(REGISTRY_XML);
        Assert.assertNotNull("The generated registry data is expected to be not null", registryData);
        String actualRegistryXml = RegistryDataUtils.generateRegistryXml(registryData);
        System.out.println("expectedRegistryXml: " + REGISTRY_XML);
        System.out.println("  actualRegistryXml: " + actualRegistryXml);
        Assert.assertNotNull("The generated registry xml is expected to be not null", actualRegistryXml);
        Assert.assertEquals("Verify the generated registry xml", REGISTRY_XML, actualRegistryXml);
    }

    /**
     * Negative test for parsing registry byte representation by passing null registry byte data.
     * @throws Exception if any error occurs while executing the test.
     */
    @Test
    public void testParseRegistryData_WithNullRegistryData() throws Exception {
        RegistryData registryData = RegistryDataUtils.parseRegistryXml(null);
        Assert.assertNull("The generated registry data is expected to be null", registryData);
    }
}
