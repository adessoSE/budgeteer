package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.wickedsource.budgeteer.imports.api.ImportFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ImportFileUnzipper {

    private InputStream zipInputStream;

    /**
     * Constructs a new ImportFileUnzipper.
     *
     * @param zipInputStream the input stream containing the ZIP file.
     */
    public ImportFileUnzipper(InputStream zipInputStream) {
        this.zipInputStream = zipInputStream;
    }

    /**
     * Reads all files from the zip file and converts them into ImportFile objects.
     *
     * @return list if ImportFile objects that were read from the zip file.
     * @throws IOException in case of an error while unzipping.
     */
    public List<ImportFile> readImportFiles() throws IOException {
        List<ImportFile> importFiles = new ArrayList<ImportFile>();
        ZipInputStream zipStream = new ZipInputStream(zipInputStream);
        ZipEntry entry = zipStream.getNextEntry();
        while (entry != null) {
            if (!entry.isDirectory()) {
                InputStream entryStream = getInputStreamFromZipEntry(zipStream);
                importFiles.add(new ImportFile(entry.getName(), entryStream));
            }
            zipStream.closeEntry();
            entry = zipStream.getNextEntry();
        }
        zipStream.close();
        return importFiles;
    }

    private InputStream getInputStreamFromZipEntry(ZipInputStream zipStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while ((b = zipStream.read()) != -1) {
            out.write(b);
        }
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

}
