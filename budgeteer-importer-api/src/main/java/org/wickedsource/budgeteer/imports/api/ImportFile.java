package org.wickedsource.budgeteer.imports.api;

import java.io.InputStream;

public class ImportFile {

    private String filename;

    private InputStream inputStream;

    public ImportFile(String filename, InputStream inputStream) {
        this.filename = filename;
        this.inputStream = inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
