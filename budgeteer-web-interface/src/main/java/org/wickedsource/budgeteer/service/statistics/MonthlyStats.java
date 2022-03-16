package org.wickedsource.budgeteer.service.statistics;

import de.adesso.budgeteer.common.old.MoneyUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.wickedsource.budgeteer.persistence.record.*;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

@Data
@AllArgsConstructor
public class MonthlyStats {
  private List<MonthlyAggregatedRecordWithTaxBean> planStats;
  private List<MonthlyAggregatedRecordWithTitleAndTaxBean> workStats;

  public MonthlyStats(
      long budgetId,
      WorkRecordRepository workRecordRepository,
      PlanRecordRepository planRecordRepository) {
    workStats = workRecordRepository.aggregateByMonthAndPersonForBudgetWithTax(budgetId);
    planStats = planRecordRepository.aggregateByMonthForBudgetWithTax(budgetId);
    sumPlanStats();
  }

  public MonthlyStats(
      BudgetTagFilter budgetFilter,
      WorkRecordRepository workRecordRepository,
      PlanRecordRepository planRecordRepository) {
    if (budgetFilter.getSelectedTags().isEmpty()) {
      workStats =
          workRecordRepository.aggregateByMonthAndPersonForBudgetsWithTax(
              budgetFilter.getProjectId());
      planStats =
          planRecordRepository.aggregateByMonthForBudgetsWithTax(budgetFilter.getProjectId());
    } else {
      workStats =
          workRecordRepository.aggregateByMonthAndPersonForBudgetsWithTax(
              budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
      planStats =
          planRecordRepository.aggregateByMonthForBudgetsWithTax(
              budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
    }
    sumPlanStats();
  }

  public void sumPlanStats() {
    int i = 0;
    while (i < planStats.size() - 1) {
      MonthlyAggregatedRecordWithTaxBean current = planStats.get(i);
      MonthlyAggregatedRecordWithTaxBean next = planStats.get(i + 1);
      if (current.getYear() == next.getYear()
          && current.getMonth() == next.getMonth()
          && current.getTaxRate() == next.getTaxRate()) {
        MonthlyAggregatedRecordWithTaxBean sum =
            new MonthlyAggregatedRecordWithTaxBean(
                current.getYear(),
                current.getMonth(),
                current.getHours() + next.getHours(),
                current.getValueInCents() + next.getValueInCents(),
                current.getTaxRate());
        planStats.add(i, sum);
        planStats.remove(current);
        planStats.remove(next);
      } else {
        i++;
      }
    }
  }

  /**
   * Calculate the cent values of the planList and the workList by monthly fraction.
   *
   * @param planList List with plan records
   * @param workList List with work records
   */
  public void calculateCentValuesByMonthlyFraction(
      List<WeeklyAggregatedRecordWithTaxBean> planList,
      List<WeeklyAggregatedRecordWithTitleAndTaxBean> workList) {
    for (WeeklyAggregatedRecordWithTaxBean weekRecord : planList) {
      for (MonthlyAggregatedRecordWithTaxBean monthRecord : planStats) {
        if (weekRecord.getTaxRate() == monthRecord.getTaxRate()
            && weekRecord.getYear() == monthRecord.getYear()
            && weekRecord.getMonth() == monthRecord.getMonth()) {
          weekRecord.setValueInCents(
              MoneyUtil.getCentsByHourFraction(
                  monthRecord.getValueInCents(), monthRecord.getHours(), weekRecord.getHours()));
          break;
        }
      }
    }
    for (WeeklyAggregatedRecordWithTitleAndTaxBean weekRecord : workList) {
      for (MonthlyAggregatedRecordWithTitleAndTaxBean monthRecord : workStats) {
        if (weekRecord.getTaxRate() == monthRecord.getTaxRate()
            && weekRecord.getYear() == monthRecord.getYear()
            && weekRecord.getMonth() == monthRecord.getMonth()
            && weekRecord.getTitle() == monthRecord.getTitle()) {
          weekRecord.setValueInCents(
              MoneyUtil.getCentsByHourFraction(
                  monthRecord.getValueInCents(), monthRecord.getHours(), weekRecord.getHours()));
          break;
        }
      }
    }
  }
}
