package org.wickedsource.budgeteer.imports.api;

import java.io.InputStream;

/**
 * Some data on a file that is served by each importer as an example file so that
 * the user sees what the format of the imported files should look like.
 */
public class ExampleFile {

    private InputStream inputStream;

    private String contentType;

    private String fileName;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
