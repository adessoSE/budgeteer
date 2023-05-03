package org.wickedsource.budgeteer.importer.resourceplan;

import java.util.Date;

public class DateColumn {

  private final Date date;

  private final int columnIndex;

  public DateColumn(Date date, int columnIndex) {
    this.date = date;
    this.columnIndex = columnIndex;
  }

  public Date getDate() {
    return date;
  }

  public int getColumnIndex() {
    return columnIndex;
  }
}
