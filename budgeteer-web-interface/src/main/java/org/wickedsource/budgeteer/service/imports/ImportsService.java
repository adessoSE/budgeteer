package org.wickedsource.budgeteer.service.imports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.imports.api.Importer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImportsService {

    @Autowired
    private ImporterRegistry importerRegistry;

    /**
     * Loads all data imports the given user has made from the database.
     *
     * @param userId ID of the user whose imports to load.
     * @return list of import objects.
     */
    public List<Import> loadImports(long userId) {
        List<Import> list = new ArrayList<Import>();
        for (int i = 0; i < 20; i++) {
            Import importRecord = new Import();
            importRecord.setId(1l);
            importRecord.setEndDate(new Date());
            importRecord.setStartDate(new Date());
            importRecord.setImportDate(new Date());
            importRecord.setImportType("aproda import");
            list.add(importRecord);
        }
        return list;
    }

    /**
     * Deletes all imported records that were imported within the given import.
     *
     * @param importId ID of the import whose records shall be deleted.
     */
    public void deleteImport(long importId) {

    }

    /**
     * Returns all currently registered file importers using the java ServiceLoader mechanism.
     *
     * @return all currently registered file importers.
     */
    public List<? extends Importer> getAvailableImporters() {
        return new ArrayList(importerRegistry.getWorkingRecordsImporters());
    }

}
