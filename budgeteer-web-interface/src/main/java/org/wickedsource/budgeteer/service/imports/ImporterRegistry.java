package org.wickedsource.budgeteer.service.imports;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.imports.api.PlanRecordsImporter;
import org.wickedsource.budgeteer.imports.api.WorkRecordsImporter;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

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

    public synchronized Set<PlanRecordsImporter> getPlanRecordsImporters() {
        ServiceLoader<PlanRecordsImporter> loader = ServiceLoader.load(PlanRecordsImporter.class);
        Set<PlanRecordsImporter> importers = new HashSet<PlanRecordsImporter>();
        for (PlanRecordsImporter importer : loader) {
            importers.add(importer);
        }
        return importers;
    }

}
