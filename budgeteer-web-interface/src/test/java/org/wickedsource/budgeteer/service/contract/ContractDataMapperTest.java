package org.wickedsource.budgeteer.service.contract;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;

import java.util.Optional;

class ContractDataMapperTest extends IntegrationTestTemplate {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractDataMapper testSubject;

    @Test
    @DatabaseSetup("contractMapperTest.xml")
    @DatabaseTearDown(value = "contractMapperTest.xml", type = DatabaseOperation.DELETE_ALL)
    void whenTaxrateIsNull() {
        Optional<ContractEntity> contractEntity = contractRepository.findById(3L);
        if(contractEntity.isPresent()) {
            ContractBaseData contractBaseData = testSubject.map(contractEntity.get());
            Assertions.assertThat(contractBaseData.getTaxRate()).isCloseTo(0.00, Percentage.withPercentage(10e-8));
        }else{
            Assertions.fail("Entity not found!");
        }
    }

    @Test
    @DatabaseSetup("contractMapperTest.xml")
    @DatabaseTearDown(value = "contractMapperTest.xml", type = DatabaseOperation.DELETE_ALL)
    void whenTaxrateIsNotNull() {
        Optional<ContractEntity> contractEntity = contractRepository.findById(4L);
        if (contractEntity.isPresent()) {
            ContractBaseData contractBaseData = testSubject.map(contractEntity.get());
            Assertions.assertThat(contractBaseData.getTaxRate()).isCloseTo(100, Percentage.withPercentage(10e-8));
        }else {
            Assertions.fail("Entity not found!");
        }
    }
}