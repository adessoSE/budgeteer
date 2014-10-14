package org.wickedsource.budgeteer.service.budget;

import java.util.Date;
import java.util.List;

public class BudgetDetailData {

    private long id;

    private String name;

    private List<String> tags;

    private Double total;

    private Double spent;

    private Date lastUpdated;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSpent() {
        return spent;
    }

    public void setSpent(Double spent) {
        this.spent = spent;
    }

    public Double getRemaining() {
        return this.total - this.spent;
    }

    public Double getProgress() {
        return this.getRemaining() / this.total;
    }

    /**
     * Gets the progress in percent rounded to two decimal places.
     *
     * @return the progress in percent.
     */
    public Double getProgressInPercent() {
        return getProgress() * 100;
    }
}
