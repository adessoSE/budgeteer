package de.adesso.budgeteer.service.project;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import de.adesso.budgeteer.IntegrationTestConfiguration;
import de.adesso.budgeteer.ServiceIntegrationTestTemplate;
import de.adesso.budgeteer.persistence.contract.ContractRepository;
import de.adesso.budgeteer.persistence.invoice.InvoiceRepository;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import de.adesso.budgeteer.persistence.record.PlanRecordRepository;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@TestExecutionListeners({DbUnitTestExecutionListener.class, DirtiesContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
class ProjectServiceTestWithDBUnit extends ServiceIntegrationTestTemplate {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Test
    @DatabaseSetup("deleteProject.xml")
    @DatabaseTearDown(value = "deleteProject.xml", type = DatabaseOperation.DELETE_ALL)
    void deleteEmptyProject() {
        projectService.deleteProject(1);
        Assertions.assertNull(projectRepository.findOne(1L));
    }

    @Test
    @DatabaseSetup("deleteProject.xml")
    @DatabaseTearDown(value = "deleteProject.xml", type = DatabaseOperation.DELETE_ALL)
    void deleteProject() {
        projectService.deleteProject(6);
        Assertions.assertNull(projectRepository.findOne(6L));
        Assertions.assertEquals(0, planRecordRepository.findByProjectId(6L).size());
        Assertions.assertEquals(0, workRecordRepository.findByProjectId(6L).size());
        Assertions.assertEquals(0, invoiceRepository.findByProjectId(6L).size());
        Assertions.assertEquals(0, contractRepository.findByProjectId(6L).size());
    }
}
