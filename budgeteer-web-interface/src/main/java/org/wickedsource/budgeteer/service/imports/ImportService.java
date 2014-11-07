package org.wickedsource.budgeteer.service.imports;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;
import org.wickedsource.budgeteer.imports.api.Importer;
import org.wickedsource.budgeteer.imports.api.WorkRecordsImporter;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.record.RecordRepository;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ImportService implements ApplicationContextAware {

    @Autowired
    private ImporterRegistry importerRegistry;

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private RecordRepository recordRepository;

    private ApplicationContext applicationContext;

    /**
     * Loads all data imports the given user has made from the database.
     *
     * @param projectId ID of the project whose imports to load
     * @return list of import objects.
     */
    public List<Import> loadImports(long projectId) {
        List<ImportEntity> imports = importRepository.findByProjectId(projectId);
        List<Import> resultList = new ArrayList<Import>();
        for (ImportEntity entity : imports) {
            Import i = new Import();
            i.setId(entity.getId());
            i.setImportType(entity.getImportType());
            i.setImportDate(entity.getImportDate());
            i.setEndDate(entity.getEndDate());
            i.setStartDate(entity.getStartDate());
            resultList.add(i);
        }
        return resultList;
    }

    /**
     * Deletes all imported records that were imported within the given import.
     *
     * @param importId ID of the import whose records shall be deleted.
     */
    public void deleteImport(long importId) {
        recordRepository.deleteByImport(importId);
        importRepository.delete(importId);
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
    public void doImport(long projectId, Importer importer, List<InputStream> inputStreams) throws ImportException {
        if (importer instanceof WorkRecordsImporter) {
            WorkRecordsImporter workRecordsImporter = (WorkRecordsImporter) importer;
            WorkRecordDatabaseImporter dbImporter = applicationContext.getBean(WorkRecordDatabaseImporter.class, projectId, workRecordsImporter.getDisplayName());
            for (InputStream in : inputStreams) {
                List<ImportedWorkRecord> records = workRecordsImporter.importFile(in);
                dbImporter.importRecords(records);
            }
        } else {
            throw new IllegalArgumentException(String.format("Importer of type %s is not supported!", importer.getClass()));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
