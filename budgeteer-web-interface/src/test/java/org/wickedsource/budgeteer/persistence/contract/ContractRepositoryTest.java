package org.wickedsource.budgeteer.persistence.contract;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;

import java.text.ParseException;

class ContractRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private ContractRepository repository;

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetRemainingBudgetForContract() throws ParseException {
        ContractStatisticBean records = repository.getContractStatisticAggregatedByMonthAndYear(1L, 2, 2014);
        Assertions.assertEquals(2, records.getMonth());
        Assertions.assertEquals(200.0/10000.0,records.getProgress(),10e-8);
        Assertions.assertEquals(2014, records.getYear());
        Assertions.assertEquals(10000 - 200, records.getRemainingContractBudget());
        Assertions.assertEquals(200, records.getSpentBudget());
        Assertions.assertEquals(200, records.getInvoicedBudget());

        records = repository.getContractStatisticAggregatedByMonthAndYear(1L, 6, 2015);
        Assertions.assertEquals(6, records.getMonth());
        Assertions.assertEquals(400.0/10000.0,records.getProgress(),10e-8);
        Assertions.assertEquals(2015, records.getYear());
        Assertions.assertEquals(10000 - 400, records.getRemainingContractBudget());
        Assertions.assertEquals(400, records.getSpentBudget());
        Assertions.assertEquals(400, records.getInvoicedBudget());


        records = repository.getContractStatisticAggregatedByMonthAndYear(1L, 1, 2016);
        Assertions.assertEquals(1, records.getMonth());
        Assertions.assertEquals(400.0/10000.0,records.getProgress(),10e-8);
        Assertions.assertEquals(2016, records.getYear());
        Assertions.assertEquals(10000 - 400, records.getRemainingContractBudget());
        Assertions.assertEquals(400, records.getSpentBudget());
        Assertions.assertEquals(400, records.getInvoicedBudget());
    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetRemainingBudgetForContractWithoutWorkRecordsOrInvoices() {
        ContractStatisticBean records;
        records = repository.getContractStatisticAggregatedByMonthAndYear(2L, 1, 2016);
        Assertions.assertEquals(1, records.getMonth());
        Assertions.assertEquals(0.0,records.getProgress(),10e-8);
        Assertions.assertEquals(2016, records.getYear());
        Assertions.assertEquals(10000, records.getRemainingContractBudget());
        Assertions.assertEquals(0, records.getSpentBudget());
        Assertions.assertEquals(0, records.getInvoicedBudget());
    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetBudgetLeftByContractId() {
        Double BudgetLeft1 = repository.getBudgetLeftByContractId(1L);
        Double BudgetLeft2 = repository.getBudgetLeftByContractId(2L);
        Assertions.assertEquals(9400, BudgetLeft1,10e-8);
        Assertions.assertEquals(10000, BudgetLeft2,10e-8);
    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetBudgetSpentByContractId() {
        Double BudgetSpent1 = repository.getSpentBudgetByContractId(1L);
        Double BudgetSpent2 = repository.getSpentBudgetByContractId(2L);
        Assertions.assertEquals(600, BudgetSpent1,10e-8);
        Assertions.assertEquals(0, BudgetSpent2,10e-8);
    }
    
    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetBudgetSpentGrossByContractId() {
    	Double budgetSpentGross1 = repository.getSpentBudgetGrossByContractId(1L);
    	Double budgetSpentGross2 = repository.getSpentBudgetGrossByContractId(2L);
    	Assertions.assertEquals(1200, budgetSpentGross1,10e-8);
    	Assertions.assertEquals(0, budgetSpentGross2,10e-8);
    }
    
    
}
