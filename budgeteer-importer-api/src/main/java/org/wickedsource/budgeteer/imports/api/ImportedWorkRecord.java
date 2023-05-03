package org.wickedsource.budgeteer.imports.api;

import java.util.Date;

public class ImportedWorkRecord extends ImportedRecord {

    private int minutesWorked;

    public ImportedWorkRecord() {}

    public ImportedWorkRecord(String budgetName, String personName, Date date, int minutesWorked) {
        super(budgetName, personName, date);
        this.minutesWorked = minutesWorked;
    }

    public int getMinutesWorked() {
        return minutesWorked;
    }

    public void setMinutesWorked(int minutesWorked) {
        this.minutesWorked = minutesWorked;
    }
}
