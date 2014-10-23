package org.wickedsource.budgeteer.service.imports;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.imports.api.WorkingRecordsImporter;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

@Component
public class ImporterRegistry {

    public synchronized Set<WorkingRecordsImporter> getWorkingRecordsImporters() {
        ServiceLoader<WorkingRecordsImporter> loader = ServiceLoader.load(WorkingRecordsImporter.class);
        Set<WorkingRecordsImporter> importers = new HashSet<WorkingRecordsImporter>();
        for (WorkingRecordsImporter importer : loader) {
            importers.add(importer);
        }
        return importers;
    }

}
