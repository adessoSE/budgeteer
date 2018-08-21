package org.wickedsource.budgeteer.service.contract.report;

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

import java.util.Date;
import java.util.Optional;

class ContractReportDataMapperTest extends IntegrationTestTemplate {


    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractReportDataMapper testSubject;

    @Test
    @DatabaseSetup("contractMapperTest.xml")
    @DatabaseTearDown(value = "contractMapperTest.xml", type = DatabaseOperation.DELETE_ALL)
    void whenTaxrateIsNull() {
        Optional<ContractEntity> contractEntity = contractRepository.findById(3L);
        if(contractEntity.isPresent()) {
            ContractReportData contractBaseData = testSubject.map(contractEntity.get(), new Date());
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
        if(contractEntity.isPresent()) {
            ContractReportData contractBaseData = testSubject.map(contractEntity.get(), new Date());
            Assertions.assertThat(contractBaseData.getTaxRate()).isCloseTo(1.00, Percentage.withPercentage(10e-8));
        }else{
            Assertions.fail("Entity not found!");
        }
    }
}