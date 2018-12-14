package org.wickedsource.budgeteer.service.fixedDailyRate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateEntity;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateRepository;
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
        List<FixedDailyRateEntity> entities = fixedDailyRateRepository.getFixedDailyRateByBudgetId(budgetId);
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
        entity.setDays(data.getDateRange().getNumberOfDays());
        BudgetEntity budgetEntity = budgetRepository.findOne(data.getBudgetId());
        entity.setBudget(budgetEntity);
        fixedDailyRateRepository.save(entity);

        return entity.getId();
    }

    public void deleteFixedDailyRate(long id) {
        fixedDailyRateRepository.delete(id);
    }

    public List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetWithTax(long budgetId) {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> records = new ArrayList<>();
        List<FixedDailyRate> rates = getFixedDailyRates(budgetId);

        for (FixedDailyRate rate : rates) {
            Calendar startCalendar = DateUtil.getCalendarOfDate(rate.getStartDate());
            Calendar endCalendar = DateUtil.getCalendarOfDate(rate.getEndDate());
            Calendar weekCalendar = DateUtil.getCalendarOfDate(rate.getStartDate());

            //ToDo
            if (rate.getDays() > 7) {

                weekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                int daysInStartWeek = new DateRange(rate.getStartDate(), weekCalendar.getTime()).getNumberOfDays() + 1;

                weekCalendar.setTime(rate.getEndDate());
                weekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                int daysInEndWeek = new DateRange(weekCalendar.getTime(), rate.getEndDate()).getNumberOfDays() + 1;

                records.add(new WeeklyAggregatedRecordWithTitleAndTaxBean(startCalendar.get(Calendar.YEAR),
                        startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.WEEK_OF_YEAR),
                        (long) (MoneyUtil.toDouble(rate.getMoneyAmount()) * daysInStartWeek), rate.getTaxRate(), "fixed daily rates"));
                startCalendar.add(Calendar.WEEK_OF_YEAR, 1);

                Date cur = startCalendar.getTime();

                while (startCalendar.getTime().before(rate.getEndDate())) {
                    records.add(new WeeklyAggregatedRecordWithTitleAndTaxBean(startCalendar.get(Calendar.YEAR),
                            startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.WEEK_OF_YEAR),
                            (long) (MoneyUtil.toDouble(rate.getMoneyAmount()) * 7), rate.getTaxRate(), "fixed daily rates"));

                    startCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                }
            } else {

            }
        }
        return records;
    }
}
