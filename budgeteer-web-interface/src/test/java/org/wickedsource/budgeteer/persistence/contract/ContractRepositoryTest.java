package org.wickedsource.budgeteer.persistence.contract;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ContractRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private ContractRepository repository;

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetRemainingBudgetForContract() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ContractStatisticBean records = repository.getContractStatisticAggregatedByMonthAndYear(1l, 2, 2014);
        Assert.assertEquals(2, records.getMonth());
        Assert.assertEquals(2014, records.getYear());
        Assert.assertEquals(10000 - 200, records.getRemainingContractBudget());
        Assert.assertEquals(200, records.getSpentBudget());
        Assert.assertEquals(200, records.getInvoicedBudget());

        records = repository.getContractStatisticAggregatedByMonthAndYear(1l, 6, 2015);
        Assert.assertEquals(6, records.getMonth());
        Assert.assertEquals(2015, records.getYear());
        Assert.assertEquals(10000 - 400, records.getRemainingContractBudget());
        Assert.assertEquals(400, records.getSpentBudget());
        Assert.assertEquals(400, records.getInvoicedBudget());


        records = repository.getContractStatisticAggregatedByMonthAndYear(1l, 1, 2016);
        Assert.assertEquals(1, records.getMonth());
        Assert.assertEquals(2016, records.getYear());
        Assert.assertEquals(10000 - 400, records.getRemainingContractBudget());
        Assert.assertEquals(400, records.getSpentBudget());
        Assert.assertEquals(400, records.getInvoicedBudget());


    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetRemainingBudgetForContractWithoutWorkRecordsOrInvoices() {
        ContractStatisticBean records;
        records = repository.getContractStatisticAggregatedByMonthAndYear(2l, 1, 2016);
        Assert.assertEquals(1, records.getMonth());
        Assert.assertEquals(2016, records.getYear());
        Assert.assertEquals(10000, records.getRemainingContractBudget());
        Assert.assertEquals(0, records.getSpentBudget());
        Assert.assertEquals(0, records.getInvoicedBudget());
    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetBudgetLeftByContractId() {
        Double BudgetLeft1 = repository.getBudgetLeftByContractId(1L);
        Double BudgetLeft2 = repository.getBudgetLeftByContractId(2L);
        Assert.assertEquals(9400, BudgetLeft1.doubleValue(),10e-8);
        Assert.assertEquals(10000, BudgetLeft2.doubleValue(),10e-8);
    }

    @Test
    @DatabaseSetup("contract.xml")
    @DatabaseTearDown(value = "contract.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetBudgetSpentByContractId() {
        Double BudgetSpent1 = repository.getSpentBudgetByContractId(1L);
        Double BudgetSpent2 = repository.getSpentBudgetByContractId(2L);
        Assert.assertEquals(600, BudgetSpent1.doubleValue(),10e-8);
        Assert.assertEquals(0, BudgetSpent2.doubleValue(),10e-8);
    }
    
    
    
}
