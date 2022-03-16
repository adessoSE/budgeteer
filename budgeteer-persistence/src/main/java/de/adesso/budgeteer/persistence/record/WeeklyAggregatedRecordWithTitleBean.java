package de.adesso.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;

public class WeeklyAggregatedRecordWithTitleBean extends WeeklyAggregatedRecordBean {

  @Getter @Setter private String title;

  public WeeklyAggregatedRecordWithTitleBean(
      int year, int week, Double hours, long valueInCents, String title) {
    super(year, week, hours, valueInCents);
    this.title = title;
  }
}
