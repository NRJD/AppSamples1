/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.reg;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Registry Data.
 */
@XmlRootElement(name = "RegistryData")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistryData {
    @XmlElementWrapper(name = "BookEntries")
    @XmlElement(name = "BookEntry")
    private List<BookEntry> bookEntries = new ArrayList<BookEntry>();

    public RegistryData() {
    }

    public List<BookEntry> getBookEntries() {
        return this.bookEntries;
    }

    public void addBookEntry(BookEntry bookEntry) {
        if (bookEntry != null) {
            this.bookEntries.add(bookEntry);
        }
    }

    public void addBookEntries(List<BookEntry> bookEntries) {
        if (bookEntries != null) {
            for (BookEntry bookEntry : bookEntries) {
                addBookEntry(bookEntry);
            }
        }
    }

    public void setBookEntries(List<BookEntry> bookEntries) {
        this.bookEntries = new ArrayList<BookEntry>();
        addBookEntries(bookEntries);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(256);
        toString(buffer);
        return buffer.toString();
    }

    void toString(StringBuilder buffer) {
        if (buffer != null) {
            buffer.append(this.getClass().getSimpleName());
            buffer.append("[book-entries: [size=");
            buffer.append(bookEntries.size());
            if (bookEntries.size() > 0) {
                int index = 1;
                for (BookEntry bookEntry : bookEntries) {
                    buffer.append(", ").append(index++).append(": ");
                    bookEntry.toString(buffer);
                }
            }
            buffer.append("]]");
        }
    }
}
