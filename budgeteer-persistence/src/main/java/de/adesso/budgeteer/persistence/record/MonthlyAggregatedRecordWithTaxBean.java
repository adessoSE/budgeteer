package de.adesso.budgeteer.persistence.record;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

public class MonthlyAggregatedRecordWithTaxBean extends MonthlyAggregatedRecordBean {
  @Getter @Setter private BigDecimal taxRate;

  public MonthlyAggregatedRecordWithTaxBean(
      int year, int month, Double hours, long valueInCents, BigDecimal taxRate) {
    super(year, month, hours, valueInCents);
    this.taxRate = taxRate;
  }

  public MonthlyAggregatedRecordWithTaxBean(
      int year, int month, long minutes, Money dailyRate, BigDecimal taxRate) {
    super(year, month, minutes, dailyRate);
    this.taxRate = taxRate;
  }
}
