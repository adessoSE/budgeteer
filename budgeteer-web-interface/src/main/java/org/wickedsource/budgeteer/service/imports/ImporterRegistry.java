package org.wickedsource.budgeteer.service.imports;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.imports.api.WorkRecordsImporter;

@Component
public class ImporterRegistry {

  public synchronized Set<WorkRecordsImporter> getWorkingRecordsImporters() {
    ServiceLoader<WorkRecordsImporter> loader = ServiceLoader.load(WorkRecordsImporter.class);
    Set<WorkRecordsImporter> importers = new HashSet<WorkRecordsImporter>();
    for (WorkRecordsImporter importer : loader) {
      importers.add(importer);
    }
    return importers;
  }
}
