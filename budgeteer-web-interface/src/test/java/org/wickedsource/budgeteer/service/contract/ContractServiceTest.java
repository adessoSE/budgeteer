package org.wickedsource.budgeteer.service.contract;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.apache.commons.lang3.time.DateUtils;
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
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;

import java.util.*;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@TestExecutionListeners({
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
class ContractServiceTest{


    @Autowired
    private ContractService service;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ContractRepository contractRepository;

    /**
     * Save a new Contract associated with a Project that does not have any ProjectContractFields
     */
    @Test
    @DatabaseSetup("contractTest.xml")
    @DatabaseTearDown(value = "contractTest.xml", type = DatabaseOperation.DELETE_ALL)
    void testSaveNewContract(){
        ContractBaseData testObject = new ContractBaseData();
        testObject.setBudget(MoneyUtil.createMoney(12));
        testObject.setContractId(0);
        testObject.setProjectId(1);
        testObject.setContractName("Test Contract");
        testObject.setContractAttributes(getListOfContractFields());
        testObject.getContractAttributes().add(new DynamicAttributeField("test4", "test4"));

        long newContractId = service.save(testObject);

        assertTrue(newContractId != 0L);

        ContractBaseData savedContract = service.getContractById(newContractId);
        assertEquals(MoneyUtil.createMoney(12), savedContract.getBudget());
        assertTrue(savedContract.getContractId() > 0);
        assertEquals(5, savedContract.getContractAttributes().size());

        assertEquals(5, projectRepository.findById(savedContract.getProjectId()).getContractFields().size());
    }

    /**
     * Save a new Contract associated with a Project that already has some ProjectContractFields
     */
    @Test
    @DatabaseSetup("contractTest.xml")
    @DatabaseTearDown(value = "contractTest.xml", type = DatabaseOperation.DELETE_ALL)
    void testSaveNewContract2(){
        ContractBaseData testObject = new ContractBaseData();
        testObject.setContractId(0);
        testObject.setProjectId(2);
        testObject.setContractName("Test Contract");
        testObject.setContractAttributes(getListOfContractFields());
        testObject.getContractAttributes().add(new DynamicAttributeField("test4", "test4"));

        long newContractId = service.save(testObject);

        assertTrue(newContractId != 0L);

        ContractBaseData savedContract = service.getContractById(newContractId);
        assertTrue(savedContract.getContractId() > 0);
        assertEquals(5, savedContract.getContractAttributes().size());

        assertEquals(5, projectRepository.findById(savedContract.getProjectId()).getContractFields().size());
    }

    /**
     * Update a Contract that already has some dynamic fields
     */
    @Test
    @DatabaseSetup("contractTest.xml")
    @DatabaseTearDown(value = "contractTest.xml", type = DatabaseOperation.DELETE_ALL)
    void testUpdateContract(){
        ContractBaseData testObject = service.getContractById(4);

        testObject.setBudget(MoneyUtil.createMoney(12));
        testObject.setContractName("Test Contract");
        testObject.setInternalNumber("Test Contract");
        testObject.setStartDate(new Date());
        testObject.setType(ContractEntity.ContractType.T_UND_M);
        testObject.setContractAttributes(getListOfContractFields());
        testObject.getContractAttributes().add(new DynamicAttributeField("test5", "test5"));

        long newContractId = service.save(testObject);

        assertEquals(4, newContractId);
        testObject = null;
        ContractBaseData savedContract = service.getContractById(newContractId);

        assertEquals("Test Contract", savedContract.getContractName());
        assertEquals("Test Contract", savedContract.getInternalNumber());
        assertEquals(MoneyUtil.createMoney(12), savedContract.getBudget());
        assertEquals(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), savedContract.getStartDate());
        assertEquals(ContractEntity.ContractType.T_UND_M, savedContract.getType());
        assertEquals(6, savedContract.getContractAttributes().size());
        for (int i = 0; i < 6; i++) {
            boolean found = false;
            for(DynamicAttributeField field : savedContract.getContractAttributes()){
                if(field.getName().equals(field.getValue()) && field.getValue().equals("test" + i)){
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
        assertEquals(1, savedContract.getBelongingBudgets().size());
        assertEquals(6, projectRepository.findById(savedContract.getProjectId()).getContractFields().size());
    }

    /**
     * Update a Contract that does not has dynamic fields, but is associated with a project that has already ProjectContractFields
     */
    @Test
    @DatabaseSetup("contractTest.xml")
    @DatabaseTearDown(value = "contractTest.xml", type = DatabaseOperation.DELETE_ALL)
    void testUpdateContract1(){
        ContractBaseData testObject = service.getContractById(5);

        testObject.setBudget(MoneyUtil.createMoney(12));
        testObject.setContractName("Test Contract");
        testObject.setInternalNumber("Test Contract");
        testObject.setStartDate(new Date());
        testObject.setType(ContractEntity.ContractType.T_UND_M);
        testObject.setContractAttributes(getListOfContractFields());
        testObject.getContractAttributes().add(new DynamicAttributeField("test5", "test5"));
        testObject.getContractAttributes().add(new DynamicAttributeField("test6", "test6"));

        long newContractId = service.save(testObject);

        assertEquals(5, newContractId);
        testObject = null;
        ContractBaseData savedContract = service.getContractById(newContractId);

        assertEquals("Test Contract", savedContract.getContractName());
        assertEquals("Test Contract", savedContract.getInternalNumber());
        assertEquals(MoneyUtil.createMoney(12), savedContract.getBudget());
        assertEquals(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), savedContract.getStartDate());
        assertEquals(ContractEntity.ContractType.T_UND_M, savedContract.getType());
        assertEquals(7, savedContract.getContractAttributes().size());
        for (int i = 0; i < 7; i++) {
            boolean found = false;
            for(DynamicAttributeField field : savedContract.getContractAttributes()){
                if(field.getName().equals(field.getValue()) && field.getValue().equals("test" + i)){
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
        assertEquals(0, savedContract.getBelongingBudgets().size());
        assertEquals(7, projectRepository.findById(savedContract.getProjectId()).getContractFields().size());
    }

    @Test
    @DatabaseSetup("contractTest.xml")
    @DatabaseTearDown(value = "contractTest.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetEmptyContractModel() {
        ContractBaseData baseData = service.getEmptyContractModel(1);
        assertEquals(0, baseData.getContractAttributes().size());

        baseData = service.getEmptyContractModel(3);
        assertEquals(2, baseData.getContractAttributes().size());
        assertEquals("test0", baseData.getContractAttributes().get(0).getName());
        assertEquals("test1", baseData.getContractAttributes().get(1).getName());
    }

    @Test
    @DatabaseSetup("contractTest.xml")
    @DatabaseTearDown(value = "contractTest.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetContractById() {
        ContractBaseData testObject = service.getContractById(3);

        assertEquals(3, testObject.getContractId());
        assertEquals(3, testObject.getProjectId());

        assertEquals(2, testObject.getContractAttributes().size());
        assertEquals("test0", testObject.getContractAttributes().get(0).getName());
        assertEquals("test0", testObject.getContractAttributes().get(0).getValue());
        assertEquals("test1", testObject.getContractAttributes().get(1).getName());
        assertEquals("test1", testObject.getContractAttributes().get(1).getValue());

        assertEquals(1, testObject.getBelongingBudgets().size());
        assertEquals("Budget 1", testObject.getBelongingBudgets().get(0).getName());

        assertEquals("Test", testObject.getContractName());
        assertEquals("Test", testObject.getInternalNumber());
        assertEquals(MoneyUtil.createMoney(1), testObject.getBudget());
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(testObject.getStartDate());
        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(ContractEntity.ContractType.FIXED_PRICE, testObject.getType());
    }

    @Test
    @DatabaseSetup("contractTest.xml")
    @DatabaseTearDown(value = "contractTest.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetContractByIdWithInvoices() {
        ContractBaseData testObject = service.getContractById(6);
        assertEquals(2, testObject.getBelongingInvoices().size());
    }

    private List<DynamicAttributeField> getListOfContractFields() {
        List<DynamicAttributeField> result = new LinkedList<DynamicAttributeField>();
        DynamicAttributeField data = new DynamicAttributeField();
        for(int i = 0; i < 5; i++) {
            data = new DynamicAttributeField();
            data.setName("test" + i);
            data.setValue("test" + i);
            result.add(data);
        }
        return result;
    }

    @Test
    @DatabaseSetup("contractDeletionTest.xml")
    @DatabaseTearDown(value = "contractDeletionTest.xml", type = DatabaseOperation.DELETE_ALL)
    void testDeleteContract() {
        assertNotNull(service.getContractById(3));
        assertEquals(contractRepository.findContractFieldsByContractId(3L).size(), 2);
        assertEquals(contractRepository.findContractFieldsByContractId(4L).size(), 1);
        service.deleteContract(3);
        assertEquals(contractRepository.findContractFieldsByContractId(3L).size(), 0);
        assertEquals(contractRepository.findContractFieldsByContractId(4L).size(), 1);
        assertNull(service.getContractById(3));
    }




}
