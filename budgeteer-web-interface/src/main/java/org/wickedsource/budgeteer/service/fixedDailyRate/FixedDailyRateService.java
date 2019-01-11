package org.wickedsource.budgeteer.service.fixedDailyRate;

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

    /**
     * Get all fixed daily rate of a budget
     *
     * @param budgetId ID of the budget
     * @return fixed daily rates of the given budget
     */
    public List<FixedDailyRate> getFixedDailyRates(long budgetId) {
        List<FixedDailyRateEntity> entities = fixedDailyRateRepository.getFixedDailyRateEntitesByBudgetId(budgetId);
        List<FixedDailyRate> result = new ArrayList<>();
        for (FixedDailyRateEntity entity : entities) {
            FixedDailyRate data = new FixedDailyRate(entity);
            result.add(data);
        }
        return result;
    }

    /**
     * Get a fixed daily rate by its ID
     *
     * @param fixedDailyRateId the ID of the searched fixed daily rate
     * @return the fixed daily rate with the given ID
     */
    public FixedDailyRate loadFixedDailyRate(long fixedDailyRateId) {
        FixedDailyRateEntity entity = fixedDailyRateRepository.findOne(fixedDailyRateId);
        if(entity != null) {
            return new FixedDailyRate(entity);
        }
        return null;
    }

    /**
     * Save a fixed daily rate in the database
     *
     * @param data the fixed daily rate to save
     * @return the ID of the saved fixed daily rate
     */
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

    /**
     * Deletes a fixed daily rate from the database
     *
     * @param id the ID if the fixed daily rate, which should be deleted
     */
    public void deleteFixedDailyRate(long id) {
        fixedDailyRateRepository.delete(id);
    }

    /**
     * Get the total net cents of all fixed daily rates of a given contract until a given month and year
     *
     * @param contractId the ID of the contract
     * @param month      the month
     * @param year       the year
     * @return total net cents of all fixed daily rates of the contract until the month and year
     */
    public double getCentsOfContractTillMonthAndYear(long contractId, int month, int year) {
        List<FixedDailyRate> rates = fixedDailyRateRepository.getFixedDailyRatesByContract(contractId);
        return sumMoneyTillMonthAndYear(month, year, rates);
    }

    private double sumMoneyTillMonthAndYear(int month, int year, List<FixedDailyRate> rates) {
        double moneySum = 0;
        Date endDate = DateUtil.getEndOfMonth(month, year);

        for (FixedDailyRate rate : rates) {
            if (rate.getStartDate().before(endDate)) {
                Date end = endDate;
                if (rate.getEndDate().before(endDate)) {
                    end = rate.getEndDate();
                }
                int days = new DateRange(rate.getStartDate(), end).getNumberOfDays() + 1;
                moneySum += MoneyUtil.toDouble(rate.getMoneyAmount()) * days * 100;
            }
        }

        return moneySum;
    }

    /**
     * Get the total net cents of all fixed daily rates of a given contract for a given month and year
     *
     * @param contractId the ID of the contract
     * @param month      the month
     * @param year       the year
     * @return total net cents of all fixed daily rates of the contract for the month and year
     */
    public double getMoneyOfContractOfMonth(long contractId, int month, int year) {
        List<FixedDailyRate> rates = fixedDailyRateRepository.getFixedDailyRatesByContract(contractId);
        return sumMoneyOfMonth(month, year, rates);
    }

    private double sumMoneyOfMonth(int month, int year, List<FixedDailyRate> rates) {
        double moneySum = 0;
        Date monthEnd = DateUtil.getEndOfMonth(month, year);
        Date monthStart = DateUtil.getStartOfMonth(month, year);
        DateRange monthRange = new DateRange(monthStart, monthEnd);

        for (FixedDailyRate rate : rates) {
            Date startDate = monthStart;
            Date endDate = monthEnd;

            if (rate.getStartDate().before(monthEnd) && rate.getEndDate().after(monthStart)) {
                if (DateUtil.isDateInDateRange(rate.getStartDate(), monthRange)) {
                    startDate = rate.getStartDate();
                }

                if (DateUtil.isDateInDateRange(rate.getEndDate(), monthRange)) {
                    endDate = rate.getEndDate();
                }

                int days = new DateRange(startDate, endDate).getNumberOfDays() + 1;
                moneySum += MoneyUtil.toDouble(rate.getMoneyAmount()) * days * 100;
            }
        }

        return moneySum;
    }

    /**
     * Get the cent amounts of all fixed daily rates of a budget aggregated by week after a given start date
     *
     * @param budgetId  the ID of the budget
     * @param startDate the start date
     * @return Get the cent amounts of all fixed daily rates of a budget aggregated by week after a given start date
     */
    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetWithTax(long budgetId, Date startDate) {
        return aggregateWeeklyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByBudgetId(budgetId), startDate);
    }

    /**
     * @param budgetId the ID of the budget
     * @return Get the cent amounts of all fixed daily rates of a budget aggregated by week
     */
    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetWithTax(long budgetId) {
        return aggregateWeekly(fixedDailyRateRepository.getFixedDailyRatesByBudgetId(budgetId));
    }

    /**
     * @param projectId the ID of the project
     * @return Get the cent amounts of all fixed daily rates of a project aggregated by week
     */
    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(long projectId) {
        return aggregateWeekly(fixedDailyRateRepository.getFixedDailyRatesByProjectId(projectId));
    }

    /**
     * @param projectId the ID of the project
     * @param tags      List of budget tags
     * @return Get the cent amounts of all fixed daily rates of budgets with the given tags of a given project aggregated by week
     */
    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(long projectId, List<String> tags) {
        return aggregateWeekly(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(projectId, tags));
    }

    /**
     * @param projectId the ID of the project
     * @param tags      List of budget tags
     * @param startDate the start date
     * @return Get the cent amounts of all fixed daily rates of budgets with the given tags of a given project aggregated by week after a given start date
     */
    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(long projectId, List<String> tags, Date startDate) {
        return aggregateWeeklyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(projectId, tags, startDate), startDate);
    }

    /**
     * @param projectId the ID of the project
     * @param startDate the start date
     * @return Get the cent amounts of all fixed daily rates of a project aggregated by week after a given start date
     */
    public List<WeeklyAggregatedRecordBean> aggregateByWeekForProject(long projectId, Date startDate) {
        List<WeeklyAggregatedRecordBean> result = new ArrayList<>();
        result.addAll(aggregateWeeklyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndStartDate(projectId, startDate), startDate));
        return result;
    }

    /**
     * @param projectId the ID of the project
     * @param startDate the start date
     * @return Get the cent amounts of all fixed daily rates of a project aggregated by week after a given start date
     */
    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekWithTitleAndTaxForProject(long projectId, Date startDate) {
        return aggregateWeeklyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndStartDate(projectId, startDate), startDate);
    }

    /**
     * @param budgetId the ID of the budget
     * @return Get the cent amounts of all fixed daily rates of a budget aggregated by month
     */
    public List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetWithTax(long budgetId) {
        List<MonthlyAggregatedRecordWithTaxBean> result = new ArrayList<>();
        result.addAll(aggregateMonthly(fixedDailyRateRepository.getFixedDailyRatesByBudgetId(budgetId)));
        return result;
    }

    /**
     * @param projectId the ID of the project
     * @return Get the cent amounts of all fixed daily rates of a project aggregated by month
     */
    public List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthWithTax(long projectId) {
        List<MonthlyAggregatedRecordWithTaxBean> result = new ArrayList<>();
        result.addAll(aggregateMonthly(fixedDailyRateRepository.getFixedDailyRatesByProjectId(projectId)));
        return result;
    }

    /**
     * @param projectId the ID of the project
     * @param tags      List of budget tags
     * @return Get the cent amounts of all fixed daily rates of budgets with the given tags of a given project aggregated by month
     */
    public List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetTagsWithTax(long projectId, List<String> tags) {
        List<MonthlyAggregatedRecordWithTaxBean> result = new ArrayList<>();
        result.addAll(aggregateMonthly(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(projectId, tags)));
        return result;
    }

    /**
     * @param budgetId  the ID of the budget
     * @param startDate the start date
     * @return Get the cent amounts of all fixed daily rates of a given budget aggregated by month after a given start date
     */
    public List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthForBudgetWithTax(long budgetId, Date startDate) {
        return aggregateMonthlyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByBudgetId(budgetId, startDate), startDate);
    }

    /**
     * @param projectId the ID of the project
     * @param startDate the start date
     * @return Get the cent amounts of all fixed daily rates of a given project aggregated by month after a given start date
     */
    public List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthForBudgetsWithTax(long projectId, Date startDate) {
        return aggregateMonthlyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndStartDate(projectId, startDate), startDate);
    }

    /**
     * @param projectId the ID of the project
     * @param tags      the budget tags
     * @param startDate the start date
     * @return Get the cent amounts of all fixed daily rates of budgets with the given budget tags of a given project aggregated by month after a given start date
     */
    public List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthForBudgetsWithTax(long projectId, List<String> tags, Date startDate) {
        return aggregateMonthlyWithStartDate(fixedDailyRateRepository.getFixedDailyRatesByProjectIdAndTags(projectId, tags, startDate), startDate);
    }

    private List<MonthlyAggregatedRecordWithTitleAndTaxBean> getMonthlyRecordsOfRate(FixedDailyRate rate, Date startDate) {
        List<MonthlyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();

        Calendar currentCalendar = DateUtil.getCalendarOfDate(startDate);
        Calendar endCalendar = DateUtil.getCalendarOfDate(rate.getEndDate());

        // Calculate number the days of the rate in its start month
        Calendar helpCalendar = DateUtil.getCalendarOfDate(DateUtil.getEndOfMonth(currentCalendar));
        int daysInStartMonth = new DateRange(startDate, helpCalendar.getTime()).getNumberOfDays() + 1;

        // Calculate number the days of the rate in its end month
        helpCalendar = DateUtil.getCalendarOfDate(DateUtil.getEndOfMonth(endCalendar));
        helpCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInEndMonth = new DateRange(helpCalendar.getTime(), rate.getEndDate()).getNumberOfDays() + 1;

        int daysInRate = new DateRange(startDate, rate.getEndDate()).getNumberOfDays()+1;

        // Check if the rate spreads over more than one month
        if (daysInRate > DateUtil.getDaysInMonth(helpCalendar)) {
            helpCalendar.add(Calendar.DAY_OF_YEAR, -1);

            // Add records for the start month
            records.add(getMonthlyRecord(currentCalendar, rate, daysInStartMonth));
            currentCalendar.add(Calendar.MONTH, 1);

            // Add records for each middle month
            while (currentCalendar.getTime().before(helpCalendar.getTime())) {
                records.add(getMonthlyRecord(currentCalendar, rate, DateUtil.getDaysInMonth(helpCalendar)));
                currentCalendar.add(Calendar.MONTH, 1);
            }

            // Add records for the end month
            records.add(getMonthlyRecord(currentCalendar, rate, daysInEndMonth));
        } else {
            // Check if the rate is only in one month
            if (currentCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)) {
                // Add a record for the rate's month
                records.add(getMonthlyRecord(currentCalendar, rate, daysInRate));
            } else {
                // Add a record for the start and a record for the end month
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

        // Calculate the days in the start week of the rate
        Calendar helpCalendar = DateUtil.getCalendarOfDate(startDate);
        helpCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        int daysInStartWeek = new DateRange(startDate, helpCalendar.getTime()).getNumberOfDays() + 1;

        // Calculate the days in the end week of the rate
        helpCalendar.setTime(rate.getEndDate());
        helpCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int daysInEndWeek = new DateRange(helpCalendar.getTime(), rate.getEndDate()).getNumberOfDays() + 1;

        // Check if the rate spreads over more than one weeks
        if (rate.getDays() > 7) {
            helpCalendar.add(Calendar.DAY_OF_YEAR, -1);

            // Add records for the start week
            currentCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            records.add(getWeeklyRecord(currentCalendar, rate, daysInStartWeek));
            currentCalendar.add(Calendar.WEEK_OF_YEAR, 1);

            // Add records for each middle week
            while (currentCalendar.getTime().before(helpCalendar.getTime())) {
                records.add(getWeeklyRecord(currentCalendar, rate, 7));
                currentCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            }

            // Add records for the end week
            records.add(getWeeklyRecord(currentCalendar, rate, daysInEndWeek));
        } else {
            // Check if the rate spreads over only one week
            if (currentCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar.get(Calendar.WEEK_OF_YEAR)) {
                // Add a record for the rate's week
                records.add(getWeeklyRecord(currentCalendar, rate, new DateRange(startDate, rate.getEndDate()).getNumberOfDays() + 1));
            } else {
                // Add a record for the start week and a record for the end week
                records.add(getWeeklyRecord(currentCalendar, rate, daysInStartWeek));
                records.add(getWeeklyRecord(endCalendar, rate, daysInEndWeek));
            }
        }

        return records;
    }

    private List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateWeeklyWithStartDate(List<FixedDailyRate> rates, Date startDate) {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();

        for (FixedDailyRate rate : rates) {
            Date date = startDate;
            if (startDate.before(rate.getStartDate())) {
                date = rate.getStartDate();
            }

            List<WeeklyAggregatedRecordWithTitleAndTaxBean> rateRecords = getWeeklyRecordsOfRate(rate, date);
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
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        // if the year changes within the week, increase the year and month
        if (month == 11 && week == 1) {
            year++;
            month = 0;
        }

        return new WeeklyAggregatedRecordWithTitleAndTaxBean(year, month, week,
                (long) (MoneyUtil.toDouble(rate.getMoneyAmount()) * days * 100), rate.getTaxRate(),
                "fixed daily rates");
    }

    private MonthlyAggregatedRecordWithTitleAndTaxBean getMonthlyRecord(Calendar calendar, FixedDailyRate rate, int days) {
        return new MonthlyAggregatedRecordWithTitleAndTaxBean(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                (long) (MoneyUtil.toDouble(rate.getMoneyAmount()) * days * 100), "fixed daily rates", rate.getTaxRate());
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
            Date date = startDate;
            if (startDate.before(rate.getStartDate())) {
                date = rate.getStartDate();
            }
            List<MonthlyAggregatedRecordWithTitleAndTaxBean> rateRecords = getMonthlyRecordsOfRate(rate, date);
            records.addAll(rateRecords);
        }
        return records;
    }
}
