package org.wickedsource.budgeteer.persistence.manualRecords;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecordEntity;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecordRepository;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;

import java.math.BigDecimal;
import java.util.*;

public class ManualRecordRepositoryTest extends IntegrationTestTemplate {
    @Autowired
    private ManualRecordRepository manualRecordRepository;

    @Test
    @DatabaseSetup("aggregateByWeekForProject.xml")
    @DatabaseTearDown(value = "aggregateByWeekForProject.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForProject() throws Exception {
        Calendar calendar = new GregorianCalendar(2016, 1, 1);
        Date startDate = calendar.getTime();
        List<WeeklyAggregatedRecordBean> records = manualRecordRepository.aggregateByWeekForProject(1L, startDate);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(800, records.get(0).getValueInCents());
        Assertions.assertEquals(5, records.get(0).getWeek());
        Assertions.assertEquals(2016, records.get(0).getYear());

        Assertions.assertEquals(200, records.get(1).getValueInCents());
        Assertions.assertEquals(5, records.get(1).getWeek());
        Assertions.assertEquals(2017, records.get(1).getYear());
    }


    @Test
    @DatabaseSetup("aggregateByMonthForBudgetsWithTax.xml")
    @DatabaseTearDown(value = "aggregateByMonthForBudgetsWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthForBudgetsWithTax() throws Exception {
        Calendar calendar = new GregorianCalendar(2016, 1, 1);
        Date startDate = calendar.getTime();
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByMonthForBudgetsWithTax(1L, startDate);
        BigDecimal taxRate = new BigDecimal(10);
        BigDecimal taxRate2 = new BigDecimal(20);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(800, records.get(0).getValueInCents());
        Assertions.assertEquals("Manual records", records.get(0).getTitle());
        Assertions.assertEquals(2016, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(100, records.get(1).getValueInCents());
        Assertions.assertEquals("Manual records", records.get(1).getTitle());
        Assertions.assertEquals(2016, records.get(1).getYear());
        Assertions.assertEquals(1, records.get(1).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(1).getTaxRate()).isCloseTo(taxRate2, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByMonthForBudgetsWithTax.xml")
    @DatabaseTearDown(value = "aggregateByMonthForBudgetsWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthForBudgetsWithTaxWithTags() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 1");
        Calendar calendar = new GregorianCalendar(2016, 1, 1);
        Date startDate = calendar.getTime();
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByMonthForBudgetsWithTax(1L, tags, startDate);
        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(1, records.size());

        Assertions.assertEquals(800, records.get(0).getValueInCents());
        Assertions.assertEquals("Manual records", records.get(0).getTitle());
        Assertions.assertEquals(2016, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByMonthForBudgetWithTax.xml")
    @DatabaseTearDown(value = "aggregateByMonthForBudgetWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthForBudgetWithTax() throws Exception {
        Calendar calendar = new GregorianCalendar(2016, 1, 1);
        Date startDate = calendar.getTime();
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByMonthForBudgetWithTax(1L, startDate);

        BigDecimal taxRate = new BigDecimal(10);
        Assertions.assertEquals(1, records.size());

        Assertions.assertEquals(800, records.get(0).getValueInCents());
        Assertions.assertEquals("Manual records", records.get(0).getTitle());
        Assertions.assertEquals(2016, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByMonthAndBudgetWithTax.xml")
    @DatabaseTearDown(value = "aggregateByMonthAndBudgetWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthAndBudgetWithTax() throws Exception {
        List<MonthlyAggregatedRecordWithTaxBean> records = manualRecordRepository.aggregateByMonthAndBudgetWithTax(1L);
        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(800, records.get(1).getValueInCents());
        Assertions.assertEquals(2016, records.get(1).getYear());
        Assertions.assertEquals(1, records.get(1).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(400, records.get(0).getValueInCents());
        Assertions.assertEquals(2015, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByMonthWithTax.xml")
    @DatabaseTearDown(value = "aggregateByMonthWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthWithTax() throws Exception {
        List<MonthlyAggregatedRecordWithTaxBean> records = manualRecordRepository.aggregateByMonthWithTax(1L);

        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(800, records.get(1).getValueInCents());
        Assertions.assertEquals(2016, records.get(1).getYear());
        Assertions.assertEquals(1, records.get(1).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(400, records.get(0).getValueInCents());
        Assertions.assertEquals(2015, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByMonthAndBudgetTagsWithTax.xml")
    @DatabaseTearDown(value = "aggregateByMonthAndBudgetTagsWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByMonthAndBudgetTagsWithTax() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 1");
        Calendar calendar = new GregorianCalendar(2016, 1, 1);

        BigDecimal taxRate = new BigDecimal(10);

        List<MonthlyAggregatedRecordWithTaxBean> records = manualRecordRepository.aggregateByMonthAndBudgetTagsWithTax(1L, tags);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(800, records.get(1).getValueInCents());
        Assertions.assertEquals(2016, records.get(1).getYear());
        Assertions.assertEquals(1, records.get(1).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(400, records.get(0).getValueInCents());
        Assertions.assertEquals(2015, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByWeekForBudgetsWithTax.xml")
    @DatabaseTearDown(value = "aggregateByWeekForBudgetsWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetsWithTax() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 1");

        Calendar calendar = new GregorianCalendar(2016, 1, 1);
        Date startDate = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);
        BigDecimal taxRate2 = new BigDecimal(20);

        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByWeekForBudgetsWithTax(1L);

        Assertions.assertEquals(4, records.size());

        Assertions.assertEquals(400, records.get(0).getValueInCents());
        Assertions.assertEquals(2015, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        Assertions.assertEquals(5, records.get(0).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(100, records.get(1).getValueInCents());
        Assertions.assertEquals(2015, records.get(1).getYear());
        Assertions.assertEquals(1, records.get(1).getMonth());
        Assertions.assertEquals(5, records.get(1).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(1).getTaxRate()).isCloseTo(taxRate2, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(800, records.get(2).getValueInCents());
        Assertions.assertEquals(2016, records.get(2).getYear());
        Assertions.assertEquals(1, records.get(2).getMonth());
        Assertions.assertEquals(5, records.get(2).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(2).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(100, records.get(3).getValueInCents());
        Assertions.assertEquals(2016, records.get(3).getYear());
        Assertions.assertEquals(1, records.get(3).getMonth());
        Assertions.assertEquals(5, records.get(3).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(3).getTaxRate()).isCloseTo(taxRate2, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByWeekForBudgetsWithTax.xml")
    @DatabaseTearDown(value = "aggregateByWeekForBudgetsWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetsWithTaxWithTagsAndStartDate() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 1");

        Calendar calendar = new GregorianCalendar(2016, 1, 1);
        Date startDate = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);

        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByWeekForBudgetsWithTax(1L, tags, startDate);

        Assertions.assertEquals(1, records.size());

        Assertions.assertEquals(800, records.get(0).getValueInCents());
        Assertions.assertEquals(2016, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        Assertions.assertEquals(5, records.get(0).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByWeekForBudgetsWithTax.xml")
    @DatabaseTearDown(value = "aggregateByWeekForBudgetsWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetsWithTaxWithStartDate() throws Exception {
        Calendar calendar = new GregorianCalendar(2016, 1, 1);
        Date startDate = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);
        BigDecimal taxRate2 = new BigDecimal(20);

        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByWeekForBudgetsWithTax(1L, startDate);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(100, records.get(1).getValueInCents());
        Assertions.assertEquals(2016, records.get(1).getYear());
        Assertions.assertEquals(1, records.get(1).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(1).getTaxRate()).isCloseTo(taxRate2, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(800, records.get(0).getValueInCents());
        Assertions.assertEquals(2016, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByWeekForBudgetsWithTax.xml")
    @DatabaseTearDown(value = "aggregateByWeekForBudgetsWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetsWithTaxWithTags() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 1");

        BigDecimal taxRate = new BigDecimal(10);

        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByWeekForBudgetsWithTax(1L, tags);

        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(800, records.get(1).getValueInCents());
        Assertions.assertEquals(2016, records.get(1).getYear());
        Assertions.assertEquals(1, records.get(1).getMonth());
        Assertions.assertEquals(5, records.get(1).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(400, records.get(0).getValueInCents());
        Assertions.assertEquals(2015, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        Assertions.assertEquals(5, records.get(0).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("aggregateByWeekForBudgetWithTax.xml")
    @DatabaseTearDown(value = "aggregateByWeekForBudgetWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetWithTax() throws Exception {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByWeekForBudgetWithTax(1L);

        BigDecimal taxRate = new BigDecimal(10);
        Assertions.assertEquals(2, records.size());

        Assertions.assertEquals(800, records.get(1).getValueInCents());
        Assertions.assertEquals(2016, records.get(1).getYear());
        Assertions.assertEquals(1, records.get(1).getMonth());
        Assertions.assertEquals(5, records.get(1).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

        Assertions.assertEquals(400, records.get(0).getValueInCents());
        Assertions.assertEquals(2015, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        Assertions.assertEquals(5, records.get(0).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));

    }

    @Test
    @DatabaseSetup("aggregateByWeekForBudgetWithTax.xml")
    @DatabaseTearDown(value = "aggregateByWeekForBudgetWithTax.xml", type = DatabaseOperation.DELETE_ALL)
    void testAggregateByWeekForBudgetWithTaxWithStartDate() throws Exception {
        Calendar calendar = new GregorianCalendar(2016, 1, 1);
        Date startDate = calendar.getTime();
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = manualRecordRepository.aggregateByWeekForBudgetWithTax(1L, startDate);
        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(1, records.size());

        Assertions.assertEquals(800, records.get(0).getValueInCents());
        Assertions.assertEquals(2016, records.get(0).getYear());
        Assertions.assertEquals(1, records.get(0).getMonth());
        Assertions.assertEquals(5, records.get(0).getWeek());
        org.assertj.core.api.Assertions.assertThat(records.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
    }

    @Test
    @DatabaseSetup("getManualRecordByBudgetId.xml")
    @DatabaseTearDown(value = "getManualRecordByBudgetId.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetManualRecordByBudgetId() throws Exception {
        List<ManualRecordEntity> records = manualRecordRepository.getManualRecordByBudgetId(1L);
        Assertions.assertEquals(2, records.size());

        Calendar calendar = new GregorianCalendar(2016, 1, 3);
        Date date1 = calendar.getTime();
        calendar.set(Calendar.YEAR, 2015);
        Date date2 = calendar.getTime();

        Assertions.assertEquals(3, records.get(0).getDay());
        Assertions.assertEquals("manual 1", records.get(0).getDescription());
        Assertions.assertEquals(date1, records.get(0).getBillingDate());
        Assertions.assertEquals(1, records.get(0).getId());
        Assertions.assertEquals(1, records.get(0).getMonth());
        Assertions.assertEquals(5, records.get(0).getWeek());
        Assertions.assertEquals(2016, records.get(0).getYear());
        Assertions.assertEquals(date1, records.get(0).getCreationDate());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(800), records.get(0).getMoneyAmount());

        Assertions.assertEquals(3, records.get(1).getDay());
        Assertions.assertEquals("manual 1", records.get(1).getDescription());
        Assertions.assertEquals(date2, records.get(1).getBillingDate());
        Assertions.assertEquals(2, records.get(1).getId());
        Assertions.assertEquals(1, records.get(1).getMonth());
        Assertions.assertEquals(5, records.get(1).getWeek());
        Assertions.assertEquals(2015, records.get(1).getYear());
        Assertions.assertEquals(date2, records.get(1).getCreationDate());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400), records.get(1).getMoneyAmount());
    }

    @Test
    @DatabaseSetup("getSpentMoneyOfContractTillMonthAndYear.xml")
    @DatabaseTearDown(value = "getSpentMoneyOfContractTillMonthAndYear.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetSpentMoneyOfContractTillMonthAndYear() throws Exception {
        double spent = manualRecordRepository.getSpentMoneyOfContractTillMonthAndYear(1L, 5, 2016);
        Assertions.assertEquals(1300, spent);
    }

    @Test
    @DatabaseSetup("getSpentMoneyOfContractOfMonth.xml")
    @DatabaseTearDown(value = "getSpentMoneyOfContractOfMonth.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetSpentMoneyOfContractOfMonth() throws Exception {
        double spent = manualRecordRepository.getSpentMoneyOfContractOfMonth(1L, 1, 2016);
        Assertions.assertEquals(1300, spent);
    }

}
