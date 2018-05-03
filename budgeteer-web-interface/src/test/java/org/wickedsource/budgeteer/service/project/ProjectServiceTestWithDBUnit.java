package org.wickedsource.budgeteer.service.project;

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
import org.wickedsource.budgeteer.IntegrationTestConfiguration;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@TestExecutionListeners({DbUnitTestExecutionListener.class, DirtiesContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
class ProjectServiceTestWithDBUnit {

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
