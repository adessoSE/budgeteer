package org.wickedsource.budgeteer.imports.api;

import java.util.Date;

public class ImportedWorkRecord extends ImportedRecord {

  private String budgetProjectId;
  private int minutesWorked;

  public ImportedWorkRecord() {}

  public ImportedWorkRecord(
      String budgetProjectId, String budgetName, String personName, Date date, int minutesWorked) {
    super(budgetName, personName, date);
    this.budgetProjectId = budgetProjectId;
    this.minutesWorked = minutesWorked;
  }

  public String getBudgetProjectId() {
    if (budgetProjectId == null) {
      return getBudgetName();
    }
    return this.budgetProjectId;
  }

  public void setBudgetProjectId(String budgetProjectId) {
    this.budgetProjectId = budgetProjectId;
  }

  public int getMinutesWorked() {
    return minutesWorked;
  }

  public void setMinutesWorked(int minutesWorked) {
    this.minutesWorked = minutesWorked;
  }
}
