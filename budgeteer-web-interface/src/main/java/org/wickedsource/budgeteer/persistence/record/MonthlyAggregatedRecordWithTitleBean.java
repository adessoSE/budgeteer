package org.wickedsource.budgeteer.persistence.record;

public class MonthlyAggregatedRecordWithTitleBean extends MonthlyAggregatedRecordBean {

    private String title;

    public MonthlyAggregatedRecordWithTitleBean(int year, int week, Double hours, long valueInCents, String title) {
        super(year, week, hours, valueInCents);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
