package org.wickedsource.budgeteer.service.fixedDailyRate;

import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateEntity;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateRepository;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FixedDailyRateService {
    @Autowired
    private FixedDailyRateRepository fixedDailyRateRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    public List<FixedDailyRate> getFixedDailyRates(long budgetId) {
        List<FixedDailyRateEntity> entities = fixedDailyRateRepository.getFixedDailyRateEntitesByBudgetId(budgetId);
        List<FixedDailyRate> result = new ArrayList<>();
        for (FixedDailyRateEntity entity : entities) {
            FixedDailyRate data = new FixedDailyRate(entity);
            result.add(data);
        }
        return result;
    }

    public FixedDailyRate loadFixedDailyRate(long fixedDailyRateId) {
        return new FixedDailyRate(fixedDailyRateRepository.findOne(fixedDailyRateId));
    }

    public long saveFixedDailyRate(FixedDailyRate data) {
        assert data != null;
        FixedDailyRateEntity entity = new FixedDailyRateEntity();
        entity.setId(data.getId());
        entity.setMoneyAmount(data.getMoneyAmount());
        entity.setDescription(data.getDescription());
        entity.setName(data.getName());
        entity.setStartDate(data.getDateRange().getStartDate());
        entity.setEndDate(data.getDateRange().getEndDate());
        entity.setDays(data.getDateRange().getNumberOfDays() + 1);
        BudgetEntity budgetEntity = budgetRepository.findOne(data.getBudgetId());
        entity.setBudget(budgetEntity);
        fixedDailyRateRepository.save(entity);

        return entity.getId();
    }

    public void deleteFixedDailyRate(long id) {
        fixedDailyRateRepository.delete(id);
    }

    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetWithTax(long budgetId, Date startDate) {
        return aggregateWeeklyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByBudgetId(budgetId), startDate);
    }

    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetWithTax(long budgetId) {
        return aggregateWeekly(fixedDailyRateRepository.getFixedDailyRatesByBudgetId(budgetId));
    }

    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(long projectId) {
        return aggregateWeekly(fixedDailyRateRepository.getFixedDailyRatesByProjectId(projectId));
    }

    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(long projectId, List<String> tags) {
        return aggregateWeekly(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(projectId, tags));
    }

    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(long projectId, List<String> tags, Date startDate) {
        return aggregateWeeklyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(projectId, tags), startDate);
    }

    public List<WeeklyAggregatedRecordBean> aggregateByWeekForProject(long projectId, Date startDate) {
        List<WeeklyAggregatedRecordBean> result = new ArrayList<>();
        result.addAll(aggregateWeeklyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndStartDate(projectId, startDate, new Date()), startDate));
        return result;
    }

    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekWithTitleAndTaxForProject(long projectId, Date startDate) {
        return aggregateWeeklyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndStartDate(projectId, startDate, new Date()), startDate);
    }

    public List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetWithTax(long budgetId) {
        List<MonthlyAggregatedRecordWithTaxBean> result = new ArrayList<>();
        result.addAll(aggregateMonthly(fixedDailyRateRepository.getFixedDailyRatesByBudgetId(budgetId)));
        return result;
    }

    public List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthWithTax(long projectId) {
        List<MonthlyAggregatedRecordWithTaxBean> result = new ArrayList<>();
        result.addAll(aggregateMonthly(fixedDailyRateRepository.getFixedDailyRatesByProjectId(projectId)));
        return result;
    }

    public List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetTagsWithTax(long projectId, List<String> tags) {
        List<MonthlyAggregatedRecordWithTaxBean> result = new ArrayList<>();
        result.addAll(aggregateMonthly(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(projectId, tags)));
        return result;
    }

    public List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthForBudgetsWithTax(long projectId, Date startDate) {
        return aggregateMonthlyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectId(projectId), startDate);
    }

    public List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthForBudgetsWithTax(long projectId, List<String> tags, Date startDate) {
        return aggregateMonthlyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(projectId, tags), startDate);
    }

    private List<MonthlyAggregatedRecordWithTitleAndTaxBean> getMonthlyRecordsOfRate(FixedDailyRate rate, Date startDate) {
        // ToDo Test
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();

        Calendar currentCalendar = DateUtil.getCalendarOfDate(startDate);
        Calendar endCalendar = DateUtil.getCalendarOfDate(rate.getEndDate());
        Calendar helpCalendar = DateUtil.getCalendarOfDate(DateUtil.getEndOfMonth(currentCalendar.get(Calendar.MONTH)));

        int daysInStartMonth = new DateRange(startDate, helpCalendar.getTime()).getNumberOfDays() + 1;

        helpCalendar = DateUtil.getCalendarOfDate(DateUtil.getEndOfMonth(endCalendar.get(Calendar.MONTH) + 1));
        helpCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInEndMonth = new DateRange(helpCalendar.getTime(), rate.getEndDate()).getNumberOfDays() + 1;

        //ToDo: Test
        if (rate.getDays() > DateUtil.getDaysInMonth(helpCalendar.get(Calendar.MONTH))) {
            helpCalendar.add(Calendar.DAY_OF_YEAR, -1);

            records.add(getMonthlyRecord(currentCalendar, rate, daysInStartMonth));
            currentCalendar.add(Calendar.MONTH, 1);

            while (currentCalendar.getTime().before(helpCalendar.getTime())) {
                records.add(getMonthlyRecord(currentCalendar, rate, DateUtil.getDaysInMonth(helpCalendar.get(Calendar.MONTH))));
                currentCalendar.add(Calendar.MONTH, 1);
            }

            records.add(getMonthlyRecord(currentCalendar, rate, daysInEndMonth));
        } else {
            if (currentCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)) {
                records.add(getMonthlyRecord(currentCalendar, rate, rate.getDays()));
            } else {
                records.add(getMonthlyRecord(currentCalendar, rate, daysInStartMonth));
                records.add(getMonthlyRecord(endCalendar, rate, daysInEndMonth));
            }
        }
        return records;
    }

    private List<WeeklyAggregatedRecordWithTitleAndTaxBean> getWeeklyRecordsOfRate(FixedDailyRate rate, Date startDate) {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();

        Calendar currentCalendar = DateUtil.getCalendarOfDate(startDate);
        Calendar endCalendar = DateUtil.getCalendarOfDate(rate.getEndDate());
        Calendar helpCalendar = DateUtil.getCalendarOfDate(startDate);

        helpCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        int daysInStartWeek = new DateRange(startDate, helpCalendar.getTime()).getNumberOfDays() + 1;

        helpCalendar.setTime(rate.getEndDate());
        helpCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int daysInEndWeek = new DateRange(helpCalendar.getTime(), rate.getEndDate()).getNumberOfDays() + 1;

        //ToDo: Test
        if (rate.getDays() > 7) {
            helpCalendar.add(Calendar.DAY_OF_YEAR, -1);

            records.add(getWeeklyRecord(currentCalendar, rate, daysInStartWeek));
            currentCalendar.add(Calendar.WEEK_OF_YEAR, 1);

            while (currentCalendar.getTime().before(helpCalendar.getTime())) {
                records.add(getWeeklyRecord(currentCalendar, rate, 7));
                currentCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            }

            records.add(getWeeklyRecord(currentCalendar, rate, daysInEndWeek));
        } else {
            if (currentCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar.get(Calendar.WEEK_OF_YEAR)) {
                records.add(getWeeklyRecord(currentCalendar, rate, rate.getDays()));
            } else {
                records.add(getWeeklyRecord(currentCalendar, rate, daysInStartWeek));
                records.add(getWeeklyRecord(endCalendar, rate, daysInEndWeek));
            }
        }

        return records;
    }

    private List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateWeeklyWithStartDate(List<FixedDailyRate> rates, Date startDate) {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();

        for (FixedDailyRate rate : rates) {
            List<WeeklyAggregatedRecordWithTitleAndTaxBean> rateRecords = getWeeklyRecordsOfRate(rate, startDate);
            records.addAll(rateRecords);
        }
        return records;
    }

    private List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateWeekly(List<FixedDailyRate> rates) {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();

        for (FixedDailyRate rate : rates) {
            List<WeeklyAggregatedRecordWithTitleAndTaxBean> rateRecords = getWeeklyRecordsOfRate(rate, rate.getStartDate());
            records.addAll(rateRecords);
        }
        return records;
    }

    private WeeklyAggregatedRecordWithTitleAndTaxBean getWeeklyRecord(Calendar calendar, FixedDailyRate rate, int days) {
        return new WeeklyAggregatedRecordWithTitleAndTaxBean(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.WEEK_OF_YEAR),
                (long) (MoneyUtil.toDouble(rate.getMoneyAmount()) * days), rate.getTaxRate(), "fixed daily rates");
    }

    private MonthlyAggregatedRecordWithTitleAndTaxBean getMonthlyRecord(Calendar calendar, FixedDailyRate rate, int days) {
        return new MonthlyAggregatedRecordWithTitleAndTaxBean(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                (long) (MoneyUtil.toDouble(rate.getMoneyAmount()) * days), "fixed daily rates", rate.getTaxRate());
    }

    private List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateMonthly(List<FixedDailyRate> rates) {
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();

        for (FixedDailyRate rate : rates) {
            List<MonthlyAggregatedRecordWithTitleAndTaxBean> rateRecords = getMonthlyRecordsOfRate(rate, rate.getStartDate());
            records.addAll(rateRecords);
        }
        return records;
    }

    private List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateMonthlyWithStartDate(List<FixedDailyRate> rates, Date startDate) {
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();

        for (FixedDailyRate rate : rates) {
            List<MonthlyAggregatedRecordWithTitleAndTaxBean> rateRecords = getMonthlyRecordsOfRate(rate, startDate);
            records.addAll(rateRecords);
        }
        return records;
    }
}
