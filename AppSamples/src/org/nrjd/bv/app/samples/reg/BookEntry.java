/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.samples.reg;

import org.nrjd.bv.app.util.DateUtils;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * Book entry.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BookEntry {
    @XmlElement(name = "bookName")
    private String bookName;

    @XmlElement(name = "fileName")
    private String fileName;

    @XmlElement(name = "lastModified")
    private String lastModified;

    @XmlElement(name = "size")
    private long size;

    public BookEntry() {
    }

    public String getBookName() {
        return this.bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLastModified() {
        return this.lastModified;
    }

    public Date getLastModifiedDate() {
        return DateUtils.parseDate(this.lastModified);
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModified = DateUtils.formatDate(lastModifiedDate);
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
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
            buffer.append("[");
            buffer.append("bookName: " + this.getBookName());
            buffer.append(", fileName: " + this.getFileName());
            buffer.append(", lastModified: " + this.getLastModified());
            buffer.append(", size: " + this.getSize());
            buffer.append("]");
        }
    }
}
