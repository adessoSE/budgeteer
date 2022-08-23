package org.wickedsource.budgeteer.service.imports;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.wickedsource.budgeteer.importer.aproda.AprodaWorkRecordsImporter;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.InvalidFileFormatException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext
class WorkRecordImporterTest {

  @Autowired private ProjectRepository projectRepository;
  @Autowired private ImportService importService;

  private void doImport(long projectId) throws ImportException, InvalidFileFormatException {
    List<ImportFile> importFiles = new ArrayList<ImportFile>();
    importFiles.add(new ImportFile("file1", getClass().getResourceAsStream("testReport3.xlsx")));
    importService.doImport(projectId, new AprodaWorkRecordsImporter(), importFiles);
  }

  @Test
  void testGetSkippedRecordsNoSkippedRecords() throws Exception {
    var project = new ProjectEntity();
    project.setId(1L);
    project.setName("project1");
    projectRepository.save(project);
    doImport(project.getId());
    List<List<String>> skippedRecords = importService.getSkippedRecords();
    Assertions.assertEquals(3, skippedRecords.size());
  }

  @Test
  void testGetSkippedRecordsSomeSkippedRecords() throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
    ProjectEntity project = new ProjectEntity();
    project.setName("project2");
    project.setProjectStart(formatter.parse("02.01.2014"));
    project.setProjectEnd(formatter.parse("12.01.2014"));
    var savedProject = projectRepository.save(project);
    doImport(savedProject.getId());
    List<List<String>> skippedRecords = importService.getSkippedRecords();
    Assertions.assertEquals(10, skippedRecords.size());
  }
}
