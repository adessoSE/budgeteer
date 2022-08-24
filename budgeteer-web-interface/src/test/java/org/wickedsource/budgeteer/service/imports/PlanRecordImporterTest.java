package org.wickedsource.budgeteer.service.imports;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.wickedsource.budgeteer.importer.resourceplan.ResourcePlanImporter;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.InvalidFileFormatException;

@SpringBootTest
@DirtiesContext
class PlanRecordImporterTest {

  @Autowired private ProjectRepository projectRepository;
  @Autowired private ImportService importService;

  private void doImport(long projectId) throws ImportException, InvalidFileFormatException {
    List<ImportFile> importFiles = new ArrayList<ImportFile>();
    importFiles.add(
        new ImportFile(
            "resource_plan2.xlsx", getClass().getResourceAsStream("resource_plan2.xlsx")));
    importService.doImport(projectId, new ResourcePlanImporter(), importFiles);
  }

  @Test
  void testGetSkippedRecordsNoSkippedRecords() throws Exception {
    var projectEntity = new ProjectEntity();
    projectEntity.setName("project1");
    var projectId = projectRepository.save(projectEntity).getId();
    doImport(projectId);
    List<List<String>> skippedRecords = importService.getSkippedRecords();
    Assertions.assertEquals(0, skippedRecords.size());
  }

  @Test
  void testGetSkippedRecordsSomeSkippedRecords() throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
    ProjectEntity project = new ProjectEntity();
    project.setName("project2");
    project.setProjectStart(formatter.parse("02.01.2014"));
    project.setProjectEnd(formatter.parse("12.01.2014"));
    var projectId = projectRepository.save(project).getId();
    doImport(projectId);
    List<List<String>> skippedRecords = importService.getSkippedRecords();
    Assertions.assertEquals(75, skippedRecords.size());
  }
}
