package org.wickedsource.budgeteer.service.contract;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractFieldEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectContractField;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.LinkedList;
import java.util.List;


public class ContractServiceTest extends ServiceTestTemplate {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractService service;

    @Autowired
    private ProjectRepository projectRepository;


    @Test
    /**
     * Save a new Contract belonging to a Contract that does not have any ProjectContractFields
     */
    public void testSaveNewContract(){
        Mockito.when(projectRepository.findOne(Mockito.anyLong())).thenReturn(getProjectWithoutProjectContractAttributes());
        ContractBaseData testObject = new ContractBaseData();
        testObject.setBudget(MoneyUtil.createMoney(12));
        testObject.setContractId(0);
        testObject.setContractAttributes(getListOfContractFields());
        service.save(testObject);
    }

    @Test
    /**
     * Save a new Contract belonging to a Contract that has ProjectContractFields
     */
    public void testSaveNewContract2(){
        Mockito.when(projectRepository.findOne(Mockito.anyLong())).thenReturn(getProjectWithProjectContractAttributes());
        ContractBaseData testObject = new ContractBaseData();
        testObject.setBudget(MoneyUtil.createMoney(12));
        testObject.setContractId(0);
        testObject.setContractAttributes(getListOfContractFields());
        service.save(testObject);
    }

    @Test
    /**
     * Update Contract belonging to a Contract that does not have any ProjectContractFields
     */
    public void testUpdateContract(){
        Mockito.when(projectRepository.findOne(Mockito.anyLong())).thenReturn(getProjectWithoutProjectContractAttributes());
        ContractBaseData testObject = new ContractBaseData();
        testObject.setBudget(MoneyUtil.createMoney(12));
        testObject.setContractId(0);
        testObject.setContractAttributes(getListOfContractFields());

        Mockito.when(contractRepository.findOne(Mockito.anyLong())).thenReturn(getContractEntity());

        service.save(testObject);
    }

    @Test
    /**
     * Update Contract belonging to a Contract that has ProjectContractFields
     */
    public void testUpdateContract2(){
        Mockito.when(projectRepository.findOne(Mockito.anyLong())).thenReturn(getProjectWithProjectContractAttributes());
        ContractBaseData testObject = new ContractBaseData();
        testObject.setBudget(MoneyUtil.createMoney(12));
        testObject.setContractId(0);
        testObject.setContractAttributes(getListOfContractFields());
        Mockito.when(contractRepository.findOne(Mockito.anyLong())).thenReturn(getContractEntity());
        service.save(testObject);
    }

    private List<ContractFieldData> getListOfContractFields() {
        List<ContractFieldData> result = new LinkedList<ContractFieldData>();
        ContractFieldData data = new ContractFieldData();
        for(int i = 0; i < 5; i++) {
            data.setName("test" + i);
            data.setValue("test" + i);
            result.add(data);
        }
        return result;
    }

    private ProjectEntity getProjectWithoutProjectContractAttributes(){
        ProjectEntity result = new ProjectEntity();
        result.setContractFields(new LinkedList<ProjectContractField>());
        return result;
    }

    private ProjectEntity getProjectWithProjectContractAttributes(){
        ProjectEntity result = new ProjectEntity();
        result.setContractFields(new LinkedList<ProjectContractField>());
        ProjectContractField fieldData = new ProjectContractField();
        for (int i = 0; i < 3; i++) {
            fieldData.setId(i);
            fieldData.setFieldName("test" + i);
            result.getContractFields().add(fieldData);
        }
        return result;
    }

    private ContractEntity getContractEntity(){
        ContractEntity entity = new ContractEntity();
        for (int i = 0; i < 4; i++) {
            ContractFieldEntity field = new ContractFieldEntity();
            field.setId(i);
            field.setValue("test" + i);
            field.setField(new ProjectContractField(i, "test" + i, null));
            entity.getContractFields().add(field);
        }
        return entity;
    }




}
