package org.wickedsource.budgeteer.service.fixedDailyRate;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.assertj.core.data.Percentage;
import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
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
import org.wickedsource.budgeteer.ServiceIntegrationTestTemplate;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateRepository;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;
import org.wickedsource.budgeteer.service.DateRange;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@TestExecutionListeners({
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
public class FixedDailyRateServiceTest extends ServiceIntegrationTestTemplate {

    @Autowired
    private FixedDailyRateService fixedDailyRateService;

    @Autowired
    private FixedDailyRateRepository fixedDailyRateRepository;

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetFixedDailyRates() {
        List<FixedDailyRate> rates = fixedDailyRateService.getFixedDailyRates(1L);

        Assertions.assertEquals(2, rates.size());

        Calendar calendar = new GregorianCalendar(2018, 0, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.MONTH, 2);
        Date endDate1 = calendar.getTime();

        calendar.set(Calendar.MONTH, 1);
        Date startDate2 = calendar.getTime();

        calendar.set(Calendar.MONTH, 3);
        Date endDate2 = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(1, rates.get(0).getBudgetId());
        Assertions.assertEquals("r1", rates.get(0).getDescription());
        Assertions.assertEquals(endDate1, rates.get(0).getEndDate());
        Assertions.assertEquals(1, rates.get(0).getId());
        Assertions.assertEquals("r1", rates.get(0).getName());
        Assertions.assertEquals(startDate, rates.get(0).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(60, rates.get(0).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(0).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate1), rates.get(0).getDateRange());

        Assertions.assertEquals(1, rates.get(1).getBudgetId());
        Assertions.assertEquals("r2", rates.get(1).getDescription());
        Assertions.assertEquals(endDate2, rates.get(1).getEndDate());
        Assertions.assertEquals(2, rates.get(1).getId());
        Assertions.assertEquals("r2", rates.get(1).getName());
        Assertions.assertEquals(startDate2, rates.get(1).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(60, rates.get(1).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(1).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate2, endDate2), rates.get(1).getDateRange());
    }


    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testLoadFixedDailyRate() {
        FixedDailyRate rate = fixedDailyRateService.loadFixedDailyRate(1L);

        Calendar calendar = new GregorianCalendar(2018, 0, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.MONTH, 2);
        Date endDate1 = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(1, rate.getBudgetId());
        Assertions.assertEquals("r1", rate.getDescription());
        Assertions.assertEquals(endDate1, rate.getEndDate());
        Assertions.assertEquals(1, rate.getId());
        Assertions.assertEquals("r1", rate.getName());
        Assertions.assertEquals(startDate, rate.getStartDate());
        org.assertj.core.api.Assertions.assertThat(rate.getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(60, rate.getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rate.getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate1), rate.getDateRange());
    }


    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testSaveFixedDailyRate() {
        long budgetId = 1L;
        Money moneyAmount = MoneyUtil.createMoneyFromCents(100);
        Calendar calendar = new GregorianCalendar(2018, 0, 1);
        Date startDate = calendar.getTime();
        Date endDate = calendar.getTime();
        String description = "saved rate";
        String name = "saved rate";
        BigDecimal taxRate = new BigDecimal(10);
        int days = 1;

        FixedDailyRate rate = new FixedDailyRate();
        rate.setBudgetId(budgetId);
        rate.setEndDate(endDate);
        rate.setStartDate(startDate);
        rate.setDescription(description);
        rate.setName(name);
        rate.setTaxRate(taxRate);
        rate.setMoneyAmount(moneyAmount);
        rate.setDays(days);

        rate.setDateRange(new DateRange(startDate, endDate));

        long savedId = fixedDailyRateService.saveFixedDailyRate(rate);
        rate.setId(savedId);

        FixedDailyRate loadedRate = fixedDailyRateService.loadFixedDailyRate(savedId);
        Assertions.assertEquals(loadedRate, rate);
    }


    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testDeleteFixedDailyRate() {
        assertNotNull(fixedDailyRateService.loadFixedDailyRate(1L));
        fixedDailyRateService.deleteFixedDailyRate(1L);
        assertNull(fixedDailyRateService.loadFixedDailyRate(1L));
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetCentsOfContractTillMonthAndYear() {
        double cents = fixedDailyRateService.getCentsOfContractTillMonthAndYear(1L, 3, 2018);
        Assertions.assertEquals(19100, cents);
    }


    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetMoneyOfContractOfMonth() {
        double cents = fixedDailyRateService.getMoneyOfContractOfMonth(1L, 1, 2018);
        Assertions.assertEquals(9500, cents);
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetWithTaxWithStartDate() {
        Calendar calendar = new GregorianCalendar(2018, Calendar.FEBRUARY, 5);
        Date startDate = calendar.getTime();
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByWeekForBudgetWithTax(1L, startDate);

        Assertions.assertEquals(12, records.size());

        Assertions.assertEquals(6, records.get(0).getWeek());
        Assertions.assertEquals(700, records.get(0).getValueInCents());

        Assertions.assertEquals(7, records.get(1).getWeek());
        Assertions.assertEquals(700, records.get(1).getValueInCents());

        Assertions.assertEquals(8, records.get(2).getWeek());
        Assertions.assertEquals(700, records.get(2).getValueInCents());

        Assertions.assertEquals(9, records.get(3).getWeek());
        Assertions.assertEquals(400, records.get(3).getValueInCents());

        Assertions.assertEquals(6, records.get(4).getWeek());
        Assertions.assertEquals(700, records.get(4).getValueInCents());

        Assertions.assertEquals(7, records.get(5).getWeek());
        Assertions.assertEquals(700, records.get(5).getValueInCents());

        Assertions.assertEquals(8, records.get(6).getWeek());
        Assertions.assertEquals(700, records.get(6).getValueInCents());

        Assertions.assertEquals(9, records.get(7).getWeek());
        Assertions.assertEquals(700, records.get(7).getValueInCents());

        Assertions.assertEquals(10, records.get(8).getWeek());
        Assertions.assertEquals(700, records.get(8).getValueInCents());

        Assertions.assertEquals(11, records.get(9).getWeek());
        Assertions.assertEquals(700, records.get(9).getValueInCents());

        Assertions.assertEquals(12, records.get(10).getWeek());
        Assertions.assertEquals(700, records.get(10).getValueInCents());

        Assertions.assertEquals(13, records.get(11).getWeek());
        Assertions.assertEquals(700, records.get(11).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetWithTax() {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByWeekForBudgetWithTax(2L);

        Assertions.assertEquals(3, records.size());

        Assertions.assertEquals(2, records.get(0).getWeek());
        Assertions.assertEquals(400, records.get(0).getValueInCents());

        Assertions.assertEquals(2, records.get(1).getWeek());
        Assertions.assertEquals(200, records.get(1).getValueInCents());

        Assertions.assertEquals(3, records.get(2).getWeek());
        Assertions.assertEquals(200, records.get(2).getValueInCents());
    }


    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetsWithTax() {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByWeekForBudgetsWithTax(2L);

        Assertions.assertEquals(11, records.size());

        Assertions.assertEquals(2, records.get(0).getWeek());
        Assertions.assertEquals(600, records.get(0).getValueInCents());

        Assertions.assertEquals(3, records.get(1).getWeek());
        Assertions.assertEquals(700, records.get(1).getValueInCents());

        Assertions.assertEquals(4, records.get(2).getWeek());
        Assertions.assertEquals(700, records.get(2).getValueInCents());

        Assertions.assertEquals(5, records.get(3).getWeek());
        Assertions.assertEquals(700, records.get(3).getValueInCents());

        Assertions.assertEquals(6, records.get(4).getWeek());
        Assertions.assertEquals(700, records.get(4).getValueInCents());

        Assertions.assertEquals(7, records.get(5).getWeek());
        Assertions.assertEquals(700, records.get(5).getValueInCents());

        Assertions.assertEquals(8, records.get(6).getWeek());
        Assertions.assertEquals(700, records.get(6).getValueInCents());

        Assertions.assertEquals(9, records.get(7).getWeek());
        Assertions.assertEquals(700, records.get(7).getValueInCents());

        Assertions.assertEquals(10, records.get(8).getWeek());
        Assertions.assertEquals(200, records.get(8).getValueInCents());

        Assertions.assertEquals(1, records.get(9).getWeek());
        Assertions.assertEquals(700, records.get(9).getValueInCents());

        Assertions.assertEquals(2, records.get(10).getWeek());
        Assertions.assertEquals(100, records.get(10).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetsWithTaxWithTags() {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 3");

        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByWeekForBudgetsWithTax(1L, tags);

        Assertions.assertEquals(3, records.size());

        Assertions.assertEquals(2, records.get(0).getWeek());
        Assertions.assertEquals(400, records.get(0).getValueInCents());

        Assertions.assertEquals(2, records.get(1).getWeek());
        Assertions.assertEquals(200, records.get(1).getValueInCents());

        Assertions.assertEquals(3, records.get(2).getWeek());
        Assertions.assertEquals(200, records.get(2).getValueInCents());
    }


    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetsWithTaxWithTagsAndStartDate() {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 3");
        Calendar calendar = new GregorianCalendar(2018, 0, 15);
        Date startDate = calendar.getTime();

        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByWeekForBudgetsWithTax(1L, tags, startDate);

        Assertions.assertEquals(1, records.size());

        Assertions.assertEquals(3, records.get(0).getWeek());
        Assertions.assertEquals(200, records.get(0).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForProject() {
        Calendar calendar = new GregorianCalendar(2018, 0, 22);
        Date startDate = calendar.getTime();

        List<WeeklyAggregatedRecordBean> records = fixedDailyRateService.aggregateByWeekForProject(2L, startDate);

        Assertions.assertEquals(7, records.size());

        Assertions.assertEquals(4, records.get(0).getWeek());
        Assertions.assertEquals(700, records.get(0).getValueInCents());

        Assertions.assertEquals(5, records.get(1).getWeek());
        Assertions.assertEquals(700, records.get(1).getValueInCents());

        Assertions.assertEquals(6, records.get(2).getWeek());
        Assertions.assertEquals(700, records.get(2).getValueInCents());

        Assertions.assertEquals(7, records.get(3).getWeek());
        Assertions.assertEquals(700, records.get(3).getValueInCents());

        Assertions.assertEquals(8, records.get(4).getWeek());
        Assertions.assertEquals(700, records.get(4).getValueInCents());

        Assertions.assertEquals(9, records.get(5).getWeek());
        Assertions.assertEquals(700, records.get(5).getValueInCents());

        Assertions.assertEquals(10, records.get(6).getWeek());
        Assertions.assertEquals(200, records.get(6).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekWithTitleAndTaxForProject() {
        Calendar calendar = new GregorianCalendar(2018, 0, 22);
        Date startDate = calendar.getTime();
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByWeekWithTitleAndTaxForProject(2, startDate);

        Assertions.assertEquals(7, records.size());

        Assertions.assertEquals(4, records.get(0).getWeek());
        Assertions.assertEquals(700, records.get(0).getValueInCents());

        Assertions.assertEquals(5, records.get(1).getWeek());
        Assertions.assertEquals(700, records.get(1).getValueInCents());

        Assertions.assertEquals(6, records.get(2).getWeek());
        Assertions.assertEquals(700, records.get(2).getValueInCents());

        Assertions.assertEquals(7, records.get(3).getWeek());
        Assertions.assertEquals(700, records.get(3).getValueInCents());

        Assertions.assertEquals(8, records.get(4).getWeek());
        Assertions.assertEquals(700, records.get(4).getValueInCents());

        Assertions.assertEquals(9, records.get(5).getWeek());
        Assertions.assertEquals(700, records.get(5).getValueInCents());

        Assertions.assertEquals(10, records.get(6).getWeek());
        Assertions.assertEquals(200, records.get(6).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthAndBudgetWithTax() {
        List<MonthlyAggregatedRecordWithTaxBean> records = fixedDailyRateService.aggregateByMonthAndBudgetWithTax(3L);

        Assertions.assertEquals(4, records.size());

        Assertions.assertEquals(0, records.get(0).getMonth());
        Assertions.assertEquals(700, records.get(0).getValueInCents());

        Assertions.assertEquals(1, records.get(1).getMonth());
        Assertions.assertEquals(1200, records.get(1).getValueInCents());

        Assertions.assertEquals(0, records.get(2).getMonth());
        Assertions.assertEquals(1700, records.get(2).getValueInCents());

        Assertions.assertEquals(1, records.get(3).getMonth());
        Assertions.assertEquals(2700, records.get(3).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthWithTax() {
        List<MonthlyAggregatedRecordWithTaxBean> records = fixedDailyRateService.aggregateByMonthWithTax(2L);

        Assertions.assertEquals(4, records.size());

        Assertions.assertEquals(0, records.get(0).getMonth());
        Assertions.assertEquals(2300, records.get(0).getValueInCents());

        Assertions.assertEquals(1, records.get(1).getMonth());
        Assertions.assertEquals(2800, records.get(1).getValueInCents());

        Assertions.assertEquals(2, records.get(2).getMonth());
        Assertions.assertEquals(600, records.get(2).getValueInCents());

        Assertions.assertEquals(0, records.get(3).getMonth());
        Assertions.assertEquals(800, records.get(3).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthAndBudgetTagsWithTax() {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 3");

        List<MonthlyAggregatedRecordWithTaxBean> records = fixedDailyRateService.aggregateByMonthAndBudgetTagsWithTax(1L, tags);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(0, records.get(0).getMonth());
        Assertions.assertEquals(400, records.get(0).getValueInCents());

        Assertions.assertEquals(0, records.get(1).getMonth());
        Assertions.assertEquals(400, records.get(1).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthForBudgetWithTax() {
        Calendar calendar = new GregorianCalendar(2018, 2, 1);
        Date startDate = calendar.getTime();
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByMonthForBudgetWithTax(1L, startDate);

        Assertions.assertEquals(3, records.size());

        Assertions.assertEquals(2, records.get(0).getMonth());
        Assertions.assertEquals(100, records.get(0).getValueInCents());

        Assertions.assertEquals(2, records.get(1).getMonth());
        Assertions.assertEquals(3100, records.get(1).getValueInCents());

        Assertions.assertEquals(3, records.get(2).getMonth());
        Assertions.assertEquals(100, records.get(2).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthForBudgetsWithTax() {
        Calendar calendar = new GregorianCalendar(2018, 1, 1);
        Date startDate = calendar.getTime();
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByMonthForBudgetsWithTax(2L, startDate);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(1, records.get(0).getMonth());
        Assertions.assertEquals(2800, records.get(0).getValueInCents());

        Assertions.assertEquals(2, records.get(1).getMonth());
        Assertions.assertEquals(600, records.get(1).getValueInCents());
    }

    @Test
    @DatabaseSetup("fixedDailyRate.xml")
    @DatabaseTearDown(value = "fixedDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthForBudgetsWithTaxWithTags() {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 3");
        Calendar calendar = new GregorianCalendar(2018, 0, 13);
        Date startDate = calendar.getTime();

        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = fixedDailyRateService.aggregateByMonthForBudgetsWithTax(1L, tags, startDate);

        Assertions.assertEquals(1, records.size());

        Assertions.assertEquals(0, records.get(0).getMonth());
        Assertions.assertEquals(400, records.get(0).getValueInCents());
    }
}
