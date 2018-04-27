package org.wickedsource.budgeteer.web.pages.templates.templateimport;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.wickedsource.budgeteer.imports.api.*;
import org.wickedsource.budgeteer.service.template.Template;
import java.io.IOException;

public class TemplateImporter {

    ExampleFile getExampleFile() {
        ExampleFile file = new ExampleFile();
        file.setFileName("example_template.xlsx");
        file.setInputStream(getClass().getResourceAsStream("/report-template.xlsx"));
        return file;
    }

    public Template read(ImportFile file) throws ImportException {
        try {
            return new Template().setName(file.getFilename()).setWb(new XSSFWorkbook(file.getInputStream()));
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }
}
