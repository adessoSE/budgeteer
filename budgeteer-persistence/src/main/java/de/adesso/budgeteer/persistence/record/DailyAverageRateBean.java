package de.adesso.budgeteer.persistence.record;

import de.adesso.budgeteer.common.old.MoneyUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.money.Money;

@AllArgsConstructor
@Data
public class DailyAverageRateBean {

  private int year;
  private int month;
  private int day;
  private double rate;

  public double getRateInCents() {
    return rate;
  }

  public Money getRate() {
    return MoneyUtil.createMoneyFromCents((long) Math.floor(rate));
  }
}
