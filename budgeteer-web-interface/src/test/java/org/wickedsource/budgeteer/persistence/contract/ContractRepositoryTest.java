package org.wickedsource.budgeteer.persistence.contract;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;

import java.text.ParseException;

class ContractRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private ContractRepository repository;

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetBudgetLeftByContractId() {
        Double BudgetLeft1 = repository.getBudgetLeftByContractId(1L);
        Double BudgetLeft2 = repository.getBudgetLeftByContractId(2L);

        Assertions.assertEquals(8600, BudgetLeft1,10e-8);
        Assertions.assertEquals(10000, BudgetLeft2,10e-8);
    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetBudgetOfContract() {
       double budget = repository.getBudgetOfContract(1L);
        Assertions.assertEquals(10000,budget);
    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetInvoicedBudgetTillMonthAndYear() {
        double budget = repository.getInvoicedBudgetTillMonthAndYear(1L,3,2015);
        Assertions.assertEquals(400,budget);
    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetInvoicedBudgetOfMonth() {
        double budget = repository.getInvoicedBudgetOfMonth(1L, 2,2014);
        Assertions.assertEquals(200,budget);
    }
}
