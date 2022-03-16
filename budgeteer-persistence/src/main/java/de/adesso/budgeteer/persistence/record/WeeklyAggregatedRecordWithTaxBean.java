package de.adesso.budgeteer.persistence.record;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

public class WeeklyAggregatedRecordWithTaxBean extends WeeklyAggregatedRecordBean {

  @Getter @Setter private BigDecimal taxRate;

  public WeeklyAggregatedRecordWithTaxBean(
      int year, int week, Double hours, long valueInCents, BigDecimal taxRate) {
    super(year, week, hours, valueInCents);
    this.taxRate = taxRate;
  }

  public WeeklyAggregatedRecordWithTaxBean(
      int year, int month, int week, long minutes, Money dailyRate, BigDecimal taxRate) {
    super(year, month, week, minutes, dailyRate);
    this.taxRate = taxRate;
  }
}
