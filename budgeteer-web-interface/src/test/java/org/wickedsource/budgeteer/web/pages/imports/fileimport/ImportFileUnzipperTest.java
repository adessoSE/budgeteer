package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.imports.api.ImportFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

class ImportFileUnzipperTest {

    @Test
    void testReadImportFiles() throws Exception {
        ImportFileUnzipper unzipper = new ImportFileUnzipper(getClass().getResourceAsStream("testfiles.zip"));
        List<ImportFile> files = unzipper.readImportFiles();
        Assert.assertEquals(2, files.size());

        Assert.assertEquals("test1.txt", files.get(0).getFilename());
        Assert.assertNotNull(files.get(0).getInputStream());
        Assert.assertEquals("test1", readLine(files.get(0).getInputStream()));

        Assert.assertEquals("test2.txt", files.get(1).getFilename());
        Assert.assertNotNull(files.get(1).getInputStream());
        Assert.assertEquals("test2", readLine(files.get(1).getInputStream()));
    }

    private String readLine(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader.readLine();
    }
}