package org.wickedsource.budgeteer.service.record;

import de.adesso.budgeteer.common.old.MoneyUtil;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;

@Data
@AllArgsConstructor
public class PlanAndWorkRecord {
  private int year;
  private int month;

  /*
  The week property should be -1 if the record is a monthly aggregation.
   */
  private int week;
  private Double hoursWorked;
  private Double hoursPlanned;
  private BigDecimal taxRate;
  private long valueInCentsPlanned;
  private long valueInCentsPlanned_gross;
  private long valueInCentsBurned;
  private long valueInCentsBurned_gross;

  public PlanAndWorkRecord(WeeklyAggregatedRecordWithTaxBean planBean) {
    year = planBean.getYear();
    month = planBean.getMonth();
    week = planBean.getWeek();
    hoursWorked = 0.0;
    hoursPlanned = planBean.getHours();
    taxRate = planBean.getTaxRate();
    valueInCentsPlanned = planBean.getValueInCents();
    valueInCentsPlanned_gross =
        MoneyUtil.getCentsWithTaxes(planBean.getValueInCents(), planBean.getTaxRate());
    valueInCentsBurned = 0;
    valueInCentsBurned_gross = 0;
  }

  public PlanAndWorkRecord(WeeklyAggregatedRecordWithTitleAndTaxBean workBean) {
    year = workBean.getYear();
    month = workBean.getMonth();
    week = workBean.getWeek();
    hoursPlanned = 0.0;
    hoursWorked = workBean.getHours();
    taxRate = workBean.getTaxRate();
    valueInCentsBurned = workBean.getValueInCents();
    valueInCentsBurned_gross =
        MoneyUtil.getCentsWithTaxes(workBean.getValueInCents(), workBean.getTaxRate());
    valueInCentsPlanned = 0;
    valueInCentsPlanned_gross = 0;
  }

  /***
   *
   * @param bean use the values of this bean to create a new PlanAndWorkRecord
   * @param plan true if the bean is a plan-bean, false if the bean is a work-bean
   */
  public PlanAndWorkRecord(MonthlyAggregatedRecordWithTaxBean bean, boolean plan) {
    year = bean.getYear();
    month = bean.getMonth();
    week = -1;
    taxRate = bean.getTaxRate();
    if (plan) {
      hoursWorked = 0.0;
      hoursPlanned = bean.getHours();
      valueInCentsPlanned = bean.getValueInCents();
      valueInCentsPlanned_gross =
          MoneyUtil.getCentsWithTaxes(bean.getValueInCents(), bean.getTaxRate());
      valueInCentsBurned = 0;
      valueInCentsBurned_gross = 0;
    } else {
      hoursWorked = bean.getHours();
      hoursPlanned = 0.0;
      valueInCentsPlanned = 0;
      valueInCentsPlanned_gross = 0;
      valueInCentsBurned = bean.getValueInCents();
      valueInCentsBurned_gross =
          MoneyUtil.getCentsWithTaxes(bean.getValueInCents(), bean.getTaxRate());
    }
  }
}
