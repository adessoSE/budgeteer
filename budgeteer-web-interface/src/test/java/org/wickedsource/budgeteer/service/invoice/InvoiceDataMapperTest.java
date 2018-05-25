package org.wickedsource.budgeteer.service.invoice;


import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractInvoiceField;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceEntity;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceFieldEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class InvoiceDataMapperTest {

    /**
     * Tests whether different invoiced associated with different contracts will get the same dynamic attributes
     */
    @Test
    void testMapWithDifferentProjectsButSameAttributes(){
        InvoiceDataMapper mapper = new InvoiceDataMapper();

        List<InvoiceEntity> invoiceList = new LinkedList<InvoiceEntity>();
        invoiceList.add(getDummyInvoiceEntity());
        invoiceList.add(getDummyInvoiceEntity2());

        List<InvoiceBaseData> mappedData = mapper.map(invoiceList, true);
        InvoiceBaseData firstInvoice  = mappedData.get(0);
        assertEquals(3, firstInvoice.getDynamicInvoiceFields().size());
        assertEquals("contractInvoiceField1 Name", firstInvoice.getDynamicInvoiceFields().get(0).getName());
        assertEquals("contractInvoiceField1 Value", firstInvoice.getDynamicInvoiceFields().get(0).getValue());
        assertEquals("contractInvoiceField2 Name", firstInvoice.getDynamicInvoiceFields().get(1).getName());
        assertEquals("contractInvoiceField2 Value", firstInvoice.getDynamicInvoiceFields().get(1).getValue());
        assertEquals("contractInvoiceField3 Name", firstInvoice.getDynamicInvoiceFields().get(2).getName());
        assertEquals("", firstInvoice.getDynamicInvoiceFields().get(2).getValue());

        InvoiceBaseData secondInvoice  = mappedData.get(1);
        assertEquals(3, secondInvoice.getDynamicInvoiceFields().size());
        assertEquals("contractInvoiceField1 Name", secondInvoice.getDynamicInvoiceFields().get(0).getName());
        assertEquals("", secondInvoice.getDynamicInvoiceFields().get(0).getValue());
        assertEquals("contractInvoiceField2 Name", secondInvoice.getDynamicInvoiceFields().get(1).getName());
        assertEquals("", secondInvoice.getDynamicInvoiceFields().get(1).getValue());
        assertEquals("contractInvoiceField3 Name", secondInvoice.getDynamicInvoiceFields().get(2).getName());
        assertEquals("contractInvoiceField3 Value", secondInvoice.getDynamicInvoiceFields().get(2).getValue());


    }


    @Test
    void testMap(){
        InvoiceDataMapper mapper = new InvoiceDataMapper();
        InvoiceBaseData mappedElement = mapper.map(getDummyInvoiceEntity());

        assertEquals(1, mappedElement.getInvoiceId());
        assertEquals("InvoiceEntity 1", mappedElement.getInvoiceName());
        assertEquals(MoneyUtil.createMoneyFromCents(20000), mappedElement.getSum());
        assertEquals("InvoiceEntity 1", mappedElement.getInternalNumber());
        assertEquals(2015, mappedElement.getYear());
        assertEquals(3, mappedElement.getMonth());
        assertTrue(mappedElement.isPaid());
        assertEquals("Contract 1", mappedElement.getContractName());
        assertEquals(1, mappedElement.getContractId());

        assertEquals(2, mappedElement.getDynamicInvoiceFields().size());
        assertEquals("contractInvoiceField1 Name", mappedElement.getDynamicInvoiceFields().get(0).getName());
        assertEquals("contractInvoiceField1 Value", mappedElement.getDynamicInvoiceFields().get(0).getValue());
        assertEquals("contractInvoiceField2 Name", mappedElement.getDynamicInvoiceFields().get(1).getName());
        assertEquals("contractInvoiceField2 Value", mappedElement.getDynamicInvoiceFields().get(1).getValue());
    }


    private InvoiceEntity getDummyInvoiceEntity(){
        ProjectEntity project1 = new ProjectEntity();
        project1.setId(1);
        project1.setName("Project1");

        /*
         * Set up a contract
         */
        ContractEntity contract1 = new ContractEntity();
        contract1.setId(1);
        contract1.setProject(project1);
        contract1.setName("Contract 1");
        contract1.setInvoiceFields(new HashSet<ContractInvoiceField>());

        /*
         * Add zwo ContractInvoiceFields to the contract
         */
        ContractInvoiceField contractInvoiceField1 = new ContractInvoiceField();
        contractInvoiceField1.setId(1);
        contractInvoiceField1.setContract(contract1);
        contractInvoiceField1.setFieldName("contractInvoiceField1 Name");
        contract1.getInvoiceFields().add(contractInvoiceField1);

        ContractInvoiceField contractInvoiceField2 = new ContractInvoiceField();
        contractInvoiceField2.setId(2);
        contractInvoiceField2.setContract(contract1);
        contractInvoiceField2.setFieldName("contractInvoiceField2 Name");
        contract1.getInvoiceFields().add(contractInvoiceField2);

        /*
         * Create the first Invoice
         */
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(1);
        invoiceEntity.setName("InvoiceEntity 1");
        invoiceEntity.setInvoiceSum(MoneyUtil.createMoneyFromCents(20000));
        invoiceEntity.setInternalNumber("InvoiceEntity 1");
        invoiceEntity.setYear(2015);
        invoiceEntity.setMonth(3);
        invoiceEntity.setPaidDate(new Date());

        invoiceEntity.setFileName("FileName1");
        invoiceEntity.setFile(null);
        invoiceEntity.setLink("Link1");

        invoiceEntity.setContract(contract1);
        invoiceEntity.setDynamicFields(new LinkedList<InvoiceFieldEntity>());
        /*
         * Add some Dynamic Invoice Fields
         */
        InvoiceFieldEntity dynamicField1 = new InvoiceFieldEntity();
        dynamicField1.setId(1);
        dynamicField1.setField(contractInvoiceField1);
        dynamicField1.setValue("contractInvoiceField1 Value");
        invoiceEntity.getDynamicFields().add(dynamicField1);

        InvoiceFieldEntity dynamicField2 = new InvoiceFieldEntity();
        dynamicField2.setId(2);
        dynamicField2.setField(contractInvoiceField2);
        dynamicField2.setValue("contractInvoiceField2 Value");
        invoiceEntity.getDynamicFields().add(dynamicField2);

        return invoiceEntity;
    }

    private InvoiceEntity getDummyInvoiceEntity2(){
        ProjectEntity project1 = new ProjectEntity();
        project1.setId(1);
        project1.setName("Project1");

        ContractEntity contract2 = new ContractEntity();
        contract2.setId(2);
        contract2.setProject(project1);
        contract2.setName("Contract 2");
        contract2.setInvoiceFields(new HashSet<ContractInvoiceField>());

        ContractInvoiceField contractInvoiceField1 = new ContractInvoiceField();
        contractInvoiceField1.setId(3);
        contractInvoiceField1.setContract(contract2);
        contractInvoiceField1.setFieldName("contractInvoiceField3 Name");
        contract2.getInvoiceFields().add(contractInvoiceField1);

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(2);
        invoiceEntity.setName("InvoiceEntity 2");
        invoiceEntity.setInvoiceSum(MoneyUtil.createMoneyFromCents(20000));
        invoiceEntity.setInternalNumber("InvoiceEntity 2");
        invoiceEntity.setYear(2015);
        invoiceEntity.setMonth(3);
        invoiceEntity.setPaidDate(new Date());
        invoiceEntity.setContract(contract2);
        invoiceEntity.setDynamicFields(new LinkedList<InvoiceFieldEntity>());

        InvoiceFieldEntity dynamicField1 = new InvoiceFieldEntity();
        dynamicField1.setId(3);
        dynamicField1.setField(contractInvoiceField1);
        dynamicField1.setValue("contractInvoiceField3 Value");
        invoiceEntity.getDynamicFields().add(dynamicField1);

        return invoiceEntity;
    }
}
