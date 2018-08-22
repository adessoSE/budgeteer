package org.wickedsource.budgeteer.service.imports;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.imports.api.PlanRecordsImporter;
import org.wickedsource.budgeteer.imports.api.WorkRecordsImporter;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

@Component
class ImporterRegistry {

    synchronized Set<WorkRecordsImporter> getWorkingRecordsImporters() {
        ServiceLoader<WorkRecordsImporter> loader = ServiceLoader.load(WorkRecordsImporter.class);
        Set<WorkRecordsImporter> importers = new HashSet<>();
        for (WorkRecordsImporter importer : loader) {
            importers.add(importer);
        }
        return importers;
    }

    synchronized Set<PlanRecordsImporter> getPlanRecordsImporters() {
        ServiceLoader<PlanRecordsImporter> loader = ServiceLoader.load(PlanRecordsImporter.class);
        Set<PlanRecordsImporter> importers = new HashSet<>();
        for (PlanRecordsImporter importer : loader) {
            importers.add(importer);
        }
        return importers;
    }

}
