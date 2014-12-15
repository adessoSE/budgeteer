package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.wickedsource.budgeteer.imports.api.Importer;

import java.util.ArrayList;
import java.util.List;

public class AcceptedFileExtensionsModel extends AbstractReadOnlyModel<String> {

    private List<String> acceptedExtensions = new ArrayList<String>();

    public AcceptedFileExtensionsModel(Importer importer) {
        acceptedExtensions.addAll(importer.getSupportedFileExtensions());
        acceptedExtensions.add(".zip");
    }

    @Override
    public String getObject() {
        return StringUtils.join(acceptedExtensions, ",");
    }

}
