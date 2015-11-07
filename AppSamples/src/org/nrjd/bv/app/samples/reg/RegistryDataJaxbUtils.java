/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.reg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 * Registy Data JAXB utilities.
 */
public class RegistryDataJaxbUtils {
    /**
     * Encoding used for marshalling and unmarshalling the {@code RegistryData}
     * to and from the byte representation.
     */
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public RegistryDataJaxbUtils() {
    }

    public static byte[] generateRegistryData(RegistryData registryData) {
        byte[] data = null;
        if (registryData != null) {
            ByteArrayOutputStream outputStream = null;
            Writer writer = null;
            try {
                outputStream = new ByteArrayOutputStream();
                writer = new OutputStreamWriter(outputStream, UTF8);
                writeRegistryData(registryData, writer);
                data = outputStream.toByteArray();
            } finally {
                closeQuietly(writer);
                closeQuietly(outputStream);
            }
        }
        return data;
    }

    public static RegistryData parseRegistryData(byte[] data) {
        RegistryData registryData = null;
        if ((data != null) && (data.length > 0)) {
            ByteArrayInputStream inputStream = null;
            Reader reader = null;
            try {
                inputStream = new ByteArrayInputStream(data);
                reader = new InputStreamReader(inputStream, UTF8);
                registryData = readRegistryData(reader);
            } finally {
                closeQuietly(reader);
                closeQuietly(inputStream);
            }
        }
        return registryData;
    }

    private static RegistryData readRegistryData(Reader reader) {
        RegistryData registryData = null;
        if (reader != null) {
            try {
                JAXBContext context = JAXBContext.newInstance(RegistryData.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                registryData = (RegistryData)unmarshaller.unmarshal(reader);
            } catch (JAXBException e) {
                throw new IllegalArgumentException("Error while parsing the registry xml or byte representation", e);
            }
        }
        return registryData;
    }

    private static void writeRegistryData(RegistryData registryData, Writer writer) {
        if ((registryData != null) && (writer != null)) {
            try {
                JAXBContext context = JAXBContext.newInstance(RegistryData.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(registryData, writer);
            } catch (JAXBException e) {
                throw new IllegalArgumentException("Error while generating the registry xml or byte representation", e);
            }
        }
    }

    public static void closeQuietly(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                e = null; // Bypass jaudit rule.
            }
        }
    }
}
