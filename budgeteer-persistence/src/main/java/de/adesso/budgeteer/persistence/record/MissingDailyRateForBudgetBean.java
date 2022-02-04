package de.adesso.budgeteer.persistence.record;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

public class MissingDailyRateForBudgetBean extends MissingDailyRateBean implements Serializable {

  @Getter @Setter private String budgetName;
  @Getter @Setter private long budgetId;

  public MissingDailyRateForBudgetBean(
      long personId,
      String personName,
      Date startDate,
      Date endDate,
      String budgetName,
      long budgetId) {
    super(personId, personName, startDate, endDate);
    this.budgetName = budgetName;
    this.budgetId = budgetId;
  }
}
