package org.wickedsource.budgeteer.service.invoice;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.wickedsource.budgeteer.IntegrationTestConfiguration;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractInvoiceField;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@TestExecutionListeners({
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
public class InvoiceServiceTest{


    @Autowired
    private InvoiceService service;

    @Autowired
    private ContractRepository contractRepository;


    /**
     * Save a new Invoice belonging to a Contract that does not have any ContractInvoiceFields
     */
    @Test
    @DatabaseSetup("invoiceTest.xml")
    @DatabaseTearDown(value = "invoiceTest.xml", type = DatabaseOperation.DELETE_ALL)
    public void testSaveNewInvoice(){
        InvoiceBaseData testObject = getDummyInvoice();
        testObject.setContractId(1);

        long newContractId = service.save(testObject);

        assertTrue(newContractId != 0l);

        InvoiceBaseData savedInvoice = service.getInvoiceById(newContractId);

        assertFalse(savedInvoice.isPaid());
        assertEquals(1, savedInvoice.getContractId());
        assertEquals("Test", savedInvoice.getContractName());
        assertEquals("Internal Number", savedInvoice.getInternalNumber());
        assertEquals("Invoice Name", savedInvoice.getInvoiceName());
        assertEquals(2, savedInvoice.getMonth());
        assertEquals(2015, savedInvoice.getYear());
        assertEquals(MoneyUtil.createMoney(2000), savedInvoice.getSum());
        assertTrue(savedInvoice.getInvoiceId() > 0);
        assertEquals(5, savedInvoice.getDynamicInvoiceFields().size());
        assertEquals(5, contractRepository.findOne(savedInvoice.getContractId()).getInvoiceFields().size());
    }

    /**
     * Save a new Invoice belonging to a Contract that has two ContractInvoiceFields
     */
    @Test
    @DatabaseSetup("invoiceTest.xml")
    @DatabaseTearDown(value = "invoiceTest.xml", type = DatabaseOperation.DELETE_ALL)
    public void testSaveNewInvoice2(){
        InvoiceBaseData testObject = getDummyInvoice();
        testObject.setContractId(2);

        long newContractId = service.save(testObject);

        assertTrue(newContractId != 0l);

        InvoiceBaseData savedInvoice = service.getInvoiceById(newContractId);

        assertFalse(savedInvoice.isPaid());
        assertEquals(2, savedInvoice.getContractId());
        assertEquals("Test", savedInvoice.getContractName());
        assertEquals("Internal Number", savedInvoice.getInternalNumber());
        assertEquals("Invoice Name", savedInvoice.getInvoiceName());
        assertEquals(2, savedInvoice.getMonth());
        assertEquals(2015, savedInvoice.getYear());
        assertEquals(MoneyUtil.createMoney(2000), savedInvoice.getSum());
        assertTrue(savedInvoice.getInvoiceId() > 0);
        assertEquals(5, savedInvoice.getDynamicInvoiceFields().size());

        ContractEntity contract = contractRepository.findOne(savedInvoice.getContractId());
        Set<ContractInvoiceField> contractInvoiceFields = contract.getInvoiceFields();
        assertEquals(7, contractInvoiceFields.size());
        assertTrue(contractInvoiceFields.contains(new ContractInvoiceField(3, "Test Contract Field", contract)));
        assertTrue(contractInvoiceFields.contains(new ContractInvoiceField(4, "Test Contract Field 2", contract)));
    }

    /**
     * Update a Invoice
     */
    @Test
    @DatabaseSetup("invoiceTest.xml")
    @DatabaseTearDown(value = "invoiceTest.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdateInvoice(){
        InvoiceBaseData testObject = getDummyInvoice();
        testObject.setContractId(4);
        testObject.setInvoiceId(4);

        long newContractId = service.save(testObject);

        assertTrue(newContractId != 0l);

        InvoiceBaseData savedInvoice = service.getInvoiceById(newContractId);

        assertFalse(savedInvoice.isPaid());
        assertEquals(4, savedInvoice.getContractId());
        assertEquals("Test", savedInvoice.getContractName());
        assertEquals("Internal Number", savedInvoice.getInternalNumber());
        assertEquals("Invoice Name", savedInvoice.getInvoiceName());
        assertEquals(2, savedInvoice.getMonth());
        assertEquals(2015, savedInvoice.getYear());
        assertEquals(MoneyUtil.createMoney(2000), savedInvoice.getSum());
        assertTrue(savedInvoice.getInvoiceId() > 0);
        assertEquals(5, savedInvoice.getDynamicInvoiceFields().size());

        ContractEntity contract = contractRepository.findOne(savedInvoice.getContractId());
        Set<ContractInvoiceField> contractInvoiceFields = contract.getInvoiceFields();
        assertEquals(5, contractInvoiceFields.size());
    }

    /**
     * Update a Invoice
     */
    @Test
    @DatabaseSetup("invoiceTest.xml")
    @DatabaseTearDown(value = "invoiceTest.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdateInvoice2(){
        InvoiceBaseData testObject = getDummyInvoice();
        testObject.setContractId(3);
        testObject.setInvoiceId(3);

        long newContractId = service.save(testObject);

        assertTrue(newContractId != 0l);

        InvoiceBaseData savedInvoice = service.getInvoiceById(newContractId);

        assertFalse(savedInvoice.isPaid());
        assertEquals(3, savedInvoice.getContractId());
        assertEquals("Test", savedInvoice.getContractName());
        assertEquals("Internal Number", savedInvoice.getInternalNumber());
        assertEquals("Invoice Name", savedInvoice.getInvoiceName());
        assertEquals(2, savedInvoice.getMonth());
        assertEquals(2015, savedInvoice.getYear());
        assertEquals(MoneyUtil.createMoney(2000), savedInvoice.getSum());
        assertTrue(savedInvoice.getInvoiceId() > 0);
        assertEquals(7, savedInvoice.getDynamicInvoiceFields().size());
        assertTrue(savedInvoice.getDynamicInvoiceFields().contains(new DynamicAttributeField("Test Contract Field", "Test")));
        assertTrue(savedInvoice.getDynamicInvoiceFields().contains(new DynamicAttributeField("Test Contract Field 2", "Test 2")));

        ContractEntity contract = contractRepository.findOne(savedInvoice.getContractId());
        Set<ContractInvoiceField> contractInvoiceFields = contract.getInvoiceFields();
        assertEquals(7, contractInvoiceFields.size());
    }

    /**
     * Update a Invoice
     */
    @Test
    @DatabaseSetup("invoiceTest.xml")
    @DatabaseTearDown(value = "invoiceTest.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdateInvoice3(){
        InvoiceBaseData testObject = getDummyInvoice();
        testObject.setContractId(5);
        testObject.setInvoiceId(5);
        for(DynamicAttributeField field : testObject.getDynamicInvoiceFields()){
            field.setValue("");
        }

        long newContractId = service.save(testObject);

        assertTrue(newContractId != 0l);

        InvoiceBaseData savedInvoice = service.getInvoiceById(newContractId);

        assertFalse(savedInvoice.isPaid());
        assertEquals(5, savedInvoice.getContractId());
        assertEquals("Test", savedInvoice.getContractName());
        assertEquals("Internal Number", savedInvoice.getInternalNumber());
        assertEquals("Invoice Name", savedInvoice.getInvoiceName());
        assertEquals(2, savedInvoice.getMonth());
        assertEquals(2015, savedInvoice.getYear());
        assertEquals(MoneyUtil.createMoney(2000), savedInvoice.getSum());
        assertTrue(savedInvoice.getInvoiceId() > 0);
        assertEquals(0, savedInvoice.getDynamicInvoiceFields().size());

        ContractEntity contract = contractRepository.findOne(savedInvoice.getContractId());
        Set<ContractInvoiceField> contractInvoiceFields = contract.getInvoiceFields();
        assertEquals(2, contractInvoiceFields.size());
    }


    private InvoiceBaseData getDummyInvoice(){
        InvoiceBaseData result = new InvoiceBaseData();
        result.setInvoiceId(0);
        result.setPaid(false);
        result.setContractId(1);
        result.setContractName("Test");
        result.setInternalNumber("Internal Number");
        result.setInvoiceName("Invoice Name");
        result.setMonth(2);
        result.setSum(MoneyUtil.createMoney(2000));
        result.setYear(2015);
        result.setDynamicInvoiceFields(getDummyDynamicInvoiceFields());
        return result;
    }

    private List<DynamicAttributeField> getDummyDynamicInvoiceFields() {
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

}