package org.wickedsource.budgeteer.service.contract;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.apache.wicket.util.tester.WicketTester;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.contract.ContractSortingRepository;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.web.BudgeteerApplication;
import org.wickedsource.budgeteer.web.BudgeteerSession;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ContractDataMapperTest extends IntegrationTestTemplate {

    @Autowired
    BudgeteerApplication application;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractSortingRepository contractSortingRepository;

    @Autowired
    private ContractDataMapper testSubject;

    @Test
    @DatabaseSetup("contractMapperTest.xml")
    @DatabaseTearDown(value = "contractMapperTest.xml", type = DatabaseOperation.DELETE_ALL)
    void whenTaxrateIsNull() {
        WicketTester tester = new WicketTester(application);
        User user = new User();
        user.setId(1L);
        BudgeteerSession.get().login(user);
        BudgeteerSession.get().setProjectSelected(true);
        ContractEntity contractEntity = contractRepository.findOne(3L);
        ContractBaseData contractBaseData = testSubject.map(contractEntity);
        Assertions.assertThat(contractBaseData.getTaxRate()).isCloseTo(0.00, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("contractMapperTest.xml")
    @DatabaseTearDown(value = "contractMapperTest.xml", type = DatabaseOperation.DELETE_ALL)
    void whenTaxrateIsNotNull() {
        ContractEntity contractEntity = contractRepository.findOne(4L);
        ContractBaseData contractBaseData = testSubject.map(contractEntity);
        Assertions.assertThat(contractBaseData.getTaxRate()).isCloseTo(100, Percentage.withPercentage(10e-8));
    }

}