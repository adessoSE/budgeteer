package de.adesso.budgeteer.web.pages.imports.fileimport;

import de.adesso.budgeteer.imports.api.Importer;
import org.apache.wicket.markup.html.form.upload.FileUpload;

import java.io.Serializable;
import java.util.List;

public class ImportFormBean implements Serializable{

    private List<FileUpload> filesToImport;

    private Importer importer;

    public List<FileUpload> getFilesToImport() {
        return filesToImport;
    }

    public void setFilesToImport(List<FileUpload> filesToImport) {
        this.filesToImport = filesToImport;
    }

    public Importer getImporter() {
        return importer;
    }

    public void setImporter(Importer importer) {
        this.importer = importer;
    }
}
