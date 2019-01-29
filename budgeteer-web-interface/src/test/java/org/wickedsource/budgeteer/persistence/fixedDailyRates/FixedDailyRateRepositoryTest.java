package org.wickedsource.budgeteer.persistence.fixedDailyRates;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateEntity;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateRepository;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate;

import java.math.BigDecimal;
import java.util.*;

public class FixedDailyRateRepositoryTest extends IntegrationTestTemplate {
    @Autowired
    private FixedDailyRateRepository fixedDailyRateRepository;

    @Test
    @DatabaseSetup("getFixedDailyRatesByBudgetId.xml")
    @DatabaseTearDown(value = "getFixedDailyRatesByBudgetId.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetFixedDailyRatesByBudgetId() throws Exception {
        List<FixedDailyRate> rates = fixedDailyRateRepository.getFixedDailyRatesByBudgetId(1L);
        Assertions.assertEquals(2, rates.size());

        Calendar calendar = new GregorianCalendar(2018, 0, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 10);
        Date endDate1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 2);
        Date endDate2 = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(1, rates.get(0).getBudgetId());
        Assertions.assertEquals("r1", rates.get(0).getDescription());
        Assertions.assertEquals(endDate1, rates.get(0).getEndDate());
        Assertions.assertEquals(1, rates.get(0).getId());
        Assertions.assertEquals("r1", rates.get(0).getName());
        Assertions.assertEquals(startDate, rates.get(0).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(10, rates.get(0).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(0).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate1), rates.get(0).getDateRange());

        Assertions.assertEquals(1, rates.get(1).getBudgetId());
        Assertions.assertEquals("r2", rates.get(1).getDescription());
        Assertions.assertEquals(endDate2, rates.get(1).getEndDate());
        Assertions.assertEquals(2, rates.get(1).getId());
        Assertions.assertEquals("r2", rates.get(1).getName());
        Assertions.assertEquals(startDate, rates.get(1).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(90, rates.get(1).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200), rates.get(1).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate2), rates.get(1).getDateRange());
    }


    @Test
    @DatabaseSetup("getFixedDailyRateEntitesByBudgetId.xml")
    @DatabaseTearDown(value = "getFixedDailyRateEntitesByBudgetId.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetFixedDailyRateEntitiesByBudgetId() throws Exception {
        List<FixedDailyRateEntity> entities = fixedDailyRateRepository.getFixedDailyRateEntitesByBudgetId(1L);
        Assertions.assertEquals(2, entities.size());

        Calendar calendar = new GregorianCalendar(2018, 0, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 10);
        Date endDate1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 2);
        Date endDate2 = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(1, entities.get(0).getBudget().getId());
        Assertions.assertEquals("r1", entities.get(0).getDescription());
        Assertions.assertEquals(endDate1, entities.get(0).getEndDate());
        Assertions.assertEquals(1, entities.get(0).getId());
        Assertions.assertEquals("r1", entities.get(0).getName());
        Assertions.assertEquals(startDate, entities.get(0).getStartDate());
        org.assertj.core.api.Assertions.assertThat(entities.get(0).getBudget().getContract().getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(new Integer(10), entities.get(0).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), entities.get(0).getMoneyAmount());

        Assertions.assertEquals(1, entities.get(1).getBudget().getId());
        Assertions.assertEquals("r2", entities.get(1).getDescription());
        Assertions.assertEquals(endDate2, entities.get(1).getEndDate());
        Assertions.assertEquals(2, entities.get(1).getId());
        Assertions.assertEquals("r2", entities.get(1).getName());
        Assertions.assertEquals(startDate, entities.get(1).getStartDate());
        org.assertj.core.api.Assertions.assertThat(entities.get(1).getBudget().getContract().getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(new Integer(90), entities.get(1).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200), entities.get(1).getMoneyAmount());
    }

    @Test
    @DatabaseSetup("getFixedDailyRatesByProjectId.xml")
    @DatabaseTearDown(value = "getFixedDailyRatesByProjectId.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetFixedDailyRatesByProjectId() throws Exception {
        List<FixedDailyRate> rates1 = fixedDailyRateRepository.getFixedDailyRatesByProjectId(1L);
        List<FixedDailyRate> rates2 = fixedDailyRateRepository.getFixedDailyRatesByProjectId(2L);

        Assertions.assertEquals(2, rates1.size());
        Assertions.assertEquals(1, rates2.size());

        Calendar calendar = new GregorianCalendar(2018, 0, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 10);
        Date endDate1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 2);
        Date endDate2 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 18);
        calendar.set(Calendar.MONTH, 0);
        Date endDate3 = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);
        BigDecimal taxRate2 = new BigDecimal(20);

        Assertions.assertEquals(1, rates1.get(0).getBudgetId());
        Assertions.assertEquals("r1", rates1.get(0).getDescription());
        Assertions.assertEquals(endDate1, rates1.get(0).getEndDate());
        Assertions.assertEquals(1, rates1.get(0).getId());
        Assertions.assertEquals("r1", rates1.get(0).getName());
        Assertions.assertEquals(startDate, rates1.get(0).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates1.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(10, rates1.get(0).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates1.get(0).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate1), rates1.get(0).getDateRange());

        Assertions.assertEquals(1, rates1.get(1).getBudgetId());
        Assertions.assertEquals("r2", rates1.get(1).getDescription());
        Assertions.assertEquals(endDate2, rates1.get(1).getEndDate());
        Assertions.assertEquals(2, rates1.get(1).getId());
        Assertions.assertEquals("r2", rates1.get(1).getName());
        Assertions.assertEquals(startDate, rates1.get(1).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates1.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(90, rates1.get(1).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200), rates1.get(1).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate2), rates1.get(1).getDateRange());

        Assertions.assertEquals(2, rates2.get(0).getBudgetId());
        Assertions.assertEquals("r3", rates2.get(0).getDescription());
        Assertions.assertEquals(endDate3, rates2.get(0).getEndDate());
        Assertions.assertEquals(3, rates2.get(0).getId());
        Assertions.assertEquals("r3", rates2.get(0).getName());
        Assertions.assertEquals(startDate, rates2.get(0).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates2.get(0).getTaxRate()).isCloseTo(taxRate2, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(18, rates2.get(0).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates2.get(0).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate3), rates2.get(0).getDateRange());
    }

    @Test
    @DatabaseSetup("getFixedDailyRatesByProjectIdAndStartDate.xml")
    @DatabaseTearDown(value = "getFixedDailyRatesByProjectIdAndStartDate.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetFixedDailyRatesByProjectIdAndStartDate() throws Exception {
        Calendar calendar = new GregorianCalendar(2018, 0, 5);
        Date queryStartDate = calendar.getTime();

        List<FixedDailyRate> rates = fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndStartDate(1L, queryStartDate);
        Assertions.assertEquals(3, rates.size());

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 5);
        Date endDate1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 6);
        Date endDate2 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 18);
        Date endDate3 = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(1, rates.get(0).getBudgetId());
        Assertions.assertEquals("r3", rates.get(0).getDescription());
        Assertions.assertEquals(endDate1, rates.get(0).getEndDate());
        Assertions.assertEquals(3, rates.get(0).getId());
        Assertions.assertEquals("r3", rates.get(0).getName());
        Assertions.assertEquals(startDate, rates.get(0).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(5, rates.get(0).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(0).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate1), rates.get(0).getDateRange());

        Assertions.assertEquals(1, rates.get(1).getBudgetId());
        Assertions.assertEquals("r4", rates.get(1).getDescription());
        Assertions.assertEquals(endDate2, rates.get(1).getEndDate());
        Assertions.assertEquals(4, rates.get(1).getId());
        Assertions.assertEquals("r4", rates.get(1).getName());
        Assertions.assertEquals(startDate, rates.get(1).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(6, rates.get(1).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(1).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate2), rates.get(1).getDateRange());

        Assertions.assertEquals(1, rates.get(2).getBudgetId());
        Assertions.assertEquals("r6", rates.get(2).getDescription());
        Assertions.assertEquals(endDate3, rates.get(2).getEndDate());
        Assertions.assertEquals(6, rates.get(2).getId());
        Assertions.assertEquals("r6", rates.get(2).getName());
        Assertions.assertEquals(startDate, rates.get(2).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(2).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(18, rates.get(2).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(2).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate3), rates.get(2).getDateRange());
    }

    @Test
    @DatabaseSetup("getFixedDailyRatesByProjectIdAndTags.xml")
    @DatabaseTearDown(value = "getFixedDailyRatesByProjectIdAndTags.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetFixedDailyRatesByProjectIdAndTags() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Tag 3");

        List<FixedDailyRate> rates = fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(1L, tags);
        Assertions.assertEquals(2, rates.size());

        Calendar calendar = new GregorianCalendar(2018, 0, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 6);
        Date endDate1 = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(2, rates.get(0).getBudgetId());
        Assertions.assertEquals("r2", rates.get(0).getDescription());
        Assertions.assertEquals(endDate1, rates.get(0).getEndDate());
        Assertions.assertEquals(2, rates.get(0).getId());
        Assertions.assertEquals("r2", rates.get(0).getName());
        Assertions.assertEquals(startDate, rates.get(0).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(6, rates.get(0).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(0).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate1), rates.get(0).getDateRange());

        Assertions.assertEquals(3, rates.get(1).getBudgetId());
        Assertions.assertEquals("r3", rates.get(1).getDescription());
        Assertions.assertEquals(endDate1, rates.get(1).getEndDate());
        Assertions.assertEquals(3, rates.get(1).getId());
        Assertions.assertEquals("r3", rates.get(1).getName());
        Assertions.assertEquals(startDate, rates.get(1).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(1).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(6, rates.get(1).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(1).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate1), rates.get(1).getDateRange());
    }

    @Test
    @DatabaseSetup("getFixedDailyRatesByContract.xml")
    @DatabaseTearDown(value = "getFixedDailyRatesByContract.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetFixedDailyRatesByContract() throws Exception {
        List<FixedDailyRate> rates = fixedDailyRateRepository.getFixedDailyRatesByContract(1L);
        Assertions.assertEquals(1, rates.size());

        Calendar calendar = new GregorianCalendar(2018, 0, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 10);
        Date endDate1 = calendar.getTime();

        BigDecimal taxRate = new BigDecimal(10);

        Assertions.assertEquals(1, rates.get(0).getBudgetId());
        Assertions.assertEquals("r1", rates.get(0).getDescription());
        Assertions.assertEquals(endDate1, rates.get(0).getEndDate());
        Assertions.assertEquals(1, rates.get(0).getId());
        Assertions.assertEquals("r1", rates.get(0).getName());
        Assertions.assertEquals(startDate, rates.get(0).getStartDate());
        org.assertj.core.api.Assertions.assertThat(rates.get(0).getTaxRate()).isCloseTo(taxRate, Percentage.withPercentage(10e-8));
        Assertions.assertEquals(10, rates.get(0).getDays());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100), rates.get(0).getMoneyAmount());
        Assertions.assertEquals(new DateRange(startDate, endDate1), rates.get(0).getDateRange());
    }
}
