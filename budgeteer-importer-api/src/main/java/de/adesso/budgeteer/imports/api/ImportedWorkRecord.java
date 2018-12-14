package de.adesso.budgeteer.imports.api;

public class ImportedWorkRecord extends ImportedRecord {

    private int minutesWorked;

    public int getMinutesWorked() {
        return minutesWorked;
    }

    public void setMinutesWorked(int minutesWorked) {
        this.minutesWorked = minutesWorked;
    }
}
