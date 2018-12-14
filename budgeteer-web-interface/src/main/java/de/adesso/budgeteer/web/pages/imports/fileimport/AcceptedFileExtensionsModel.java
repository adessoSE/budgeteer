package de.adesso.budgeteer.web.pages.imports.fileimport;

import de.adesso.budgeteer.imports.api.Importer;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.AbstractReadOnlyModel;

import java.util.ArrayList;
import java.util.List;

public class AcceptedFileExtensionsModel extends AbstractReadOnlyModel<String> {

    private List<String> acceptedExtensions = new ArrayList<>();

    public AcceptedFileExtensionsModel(Importer importer) {
        acceptedExtensions.addAll(importer.getSupportedFileExtensions());
        acceptedExtensions.add(".zip");
    }

    @Override
    public String getObject() {
        return StringUtils.join(acceptedExtensions, ",");
    }

}
