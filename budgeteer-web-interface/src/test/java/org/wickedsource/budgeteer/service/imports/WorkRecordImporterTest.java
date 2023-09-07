package org.wickedsource.budgeteer.service.imports;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import de.adesso.budgeteer.persistence.budget.BudgetRepository;
import de.adesso.budgeteer.persistence.imports.ImportRepository;
import de.adesso.budgeteer.persistence.person.PersonRepository;
import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.importer.aproda.AprodaWorkRecordsImporter;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.InvalidFileFormatException;

@SpringBootTest
class WorkRecordImporterTest {

  @MockBean private ProjectRepository projectRepository;
  @MockBean private BudgetRepository budgetRepository;
  @MockBean private WorkRecordRepository workRecordRepository;
  @MockBean private PersonRepository personRepository;
  @MockBean private ImportRepository importRepository;
  @Autowired private ImportService importService;

  private void doImport() throws ImportException, InvalidFileFormatException {
    List<ImportFile> importFiles = new ArrayList<ImportFile>();
    importFiles.add(new ImportFile("file1", getClass().getResourceAsStream("testReport3.xlsx")));
    importService.doImport(1L, new AprodaWorkRecordsImporter(), importFiles);
  }

  @Test
  @DatabaseSetup("doImportWithEmptyDatabase.xml")
  @DatabaseTearDown(value = "doImportWithEmptyDatabase.xml", type = DatabaseOperation.DELETE_ALL)
  void testGetSkippedRecordsNoSkippedRecords() throws Exception {
    Mockito.when(projectRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(new ProjectEntity()));
    doImport();
    List<List<String>> skippedRecords = importService.getSkippedRecords();
    Assertions.assertEquals(3, skippedRecords.size());
  }

  @Test
  @DatabaseSetup("doImportWithEmptyDatabase.xml")
  @DatabaseTearDown(value = "doImportWithEmptyDatabase.xml", type = DatabaseOperation.DELETE_ALL)
  void testGetSkippedRecordsSomeSkippedRecords() throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
    ProjectEntity project = new ProjectEntity();
    project.setProjectStart(formatter.parse("02.01.2014"));
    project.setProjectEnd(formatter.parse("12.01.2014"));
    Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
    doImport();
    List<List<String>> skippedRecords = importService.getSkippedRecords();
    Assertions.assertEquals(10, skippedRecords.size());
  }
}
