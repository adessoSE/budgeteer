package org.wickedsource.budgeteer.imports.api;

import java.io.Serializable;
import java.util.List;

public interface Importer extends Serializable {

    String getDisplayName();

    List<String> getSupportedFileExtensions();

}
