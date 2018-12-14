package de.adesso.budgeteer.service.imports;

import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.imports.api.*;
import de.adesso.budgeteer.persistence.imports.ImportEntity;
import de.adesso.budgeteer.persistence.imports.ImportRepository;
import de.adesso.budgeteer.persistence.record.PlanRecordRepository;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import de.adesso.budgeteer.imports.api.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class ImportService implements ApplicationContextAware {

    @Autowired
    private ImporterRegistry importerRegistry;

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    private ApplicationContext applicationContext;

    @Getter
    private List<List<String>> skippedRecords;
    /**
     * Loads all data imports the given user has made from the database.
     *
     * @param projectId ID of the project whose imports to load
     * @return list of import objects.
     */
    public List<Import> loadImports(long projectId) {
        List<ImportEntity> imports = importRepository.findByProjectId(projectId);
        List<Import> resultList = new ArrayList<>();
        for (ImportEntity entity : imports) {
            Import i = new Import();
            i.setId(entity.getId());
            i.setImportType(entity.getImportType());
            i.setImportDate(entity.getImportDate());
            i.setNumberOfImportedFiles(entity.getNumberOfImportedFiles() == null ? -1 : entity.getNumberOfImportedFiles());
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
        workRecordRepository.deleteByImport(importId);
        planRecordRepository.deleteByImport(importId);
        importRepository.delete(importId);
    }

    /**
     * Returns all currently registered file importers using the java ServiceLoader mechanism.
     *
     * @return all currently registered file importers.
     */
    public List<? extends Importer> getAvailableImporters() {
        List<Importer> importers = new ArrayList<>();
        importers.addAll(importerRegistry.getWorkingRecordsImporters());
        importers.addAll(importerRegistry.getPlanRecordsImporters());
        return importers;
    }

    /**
     * Imports the data from the given inputstreams using the given importer.
     *
     * @param importer    an importer that understands the format of the files represented by the input streams.
     * @param importFiles the files to be imported
     */
    @Transactional(rollbackOn = ImportException.class)
    public void doImport(long projectId, Importer importer, List<ImportFile> importFiles) throws ImportException, InvalidFileFormatException {
        skippedRecords = new LinkedList<>();
        if (importer instanceof WorkRecordsImporter) {
            WorkRecordsImporter workRecordsImporter = (WorkRecordsImporter) importer;
            WorkRecordDatabaseImporter dbImporter = applicationContext.getBean(WorkRecordDatabaseImporter.class, projectId, workRecordsImporter.getDisplayName());
            for (ImportFile file : importFiles) {
                List<ImportedWorkRecord> records = workRecordsImporter.importFile(file);
                dbImporter.importRecords(records);
            }
            skippedRecords.addAll(workRecordsImporter.getSkippedRecords());
            skippedRecords.addAll(dbImporter.getSkippedRecords());
            skippedRecords.addAll(dbImporter.findAndRemoveManuallyEditedEntries());
        } else if (importer instanceof PlanRecordsImporter) {
            PlanRecordsImporter planRecordsImporter = (PlanRecordsImporter) importer;
            PlanRecordDatabaseImporter dbImporter = applicationContext.getBean(PlanRecordDatabaseImporter.class, projectId, planRecordsImporter.getDisplayName());
            for (ImportFile file : importFiles) {
                List<ImportedPlanRecord> records = planRecordsImporter.importFile(file, MoneyUtil.DEFAULT_CURRENCY);
                dbImporter.importRecords(records, file.getFilename());
            }
            skippedRecords.addAll(planRecordsImporter.getSkippedRecords());
            skippedRecords.addAll(dbImporter.getSkippedRecords());
        } else {
            throw new IllegalArgumentException(String.format("Importer of type %s is not supported!", importer.getClass()));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
