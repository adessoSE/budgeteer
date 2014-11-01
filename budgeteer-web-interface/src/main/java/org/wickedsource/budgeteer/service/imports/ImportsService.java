package org.wickedsource.budgeteer.service.imports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.imports.api.Importer;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ImportsService {

    @Autowired
    private ImporterRegistry importerRegistry;

    /**
     * Loads all data imports the given user has made from the database.
     *
     * @param projectId ID of the project whose imports to load
     * @return list of import objects.
     */
    public List<Import> loadImports(long projectId) {
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

    /**
     * Imports the data from the given inputstreams using the given importer.
     *
     * @param importer     an importer that understands the format of the files represented by the input streams.
     * @param inputStreams the input streams of the files to import.
     */
    public void doImport(Importer importer, List<InputStream> inputStreams) throws IOException{
    }

}
