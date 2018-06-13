package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.wickedsource.budgeteer.imports.api.Importer;

public class ImportFormBean implements Serializable {

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
