package org.wickedsource.budgeteer.service.fixedDailyRate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateEntity;
import org.wickedsource.budgeteer.service.DateRange;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Data
public class FixedDailyRate implements Serializable {
    private long id;
    private long budgetId;
    private Money moneyAmount;
    private DateRange dateRange;
    private Date startDate;
    private Date endDate;
    private String description;
    private String name;
    private BigDecimal taxRate;
    private int days;

    public FixedDailyRate(FixedDailyRateEntity entity) {
        id = entity.getId();
        budgetId = entity.getBudget().getId();
        moneyAmount = entity.getMoneyAmount();
        startDate = entity.getStartDate();
        endDate = entity.getEndDate();
        description = entity.getDescription();
        name = entity.getName();
        dateRange = new DateRange(entity.getStartDate(), entity.getEndDate());
        taxRate = entity.getBudget().getContract().getTaxRate();
        days = entity.getDays();
    }

    public FixedDailyRate(long id, long budgetId, Money moneyAmount, Date startDate, Date endDate, String description, String name, BigDecimal taxRate, int days) {
        this.id = id;
        this.budgetId = budgetId;
        this.moneyAmount = moneyAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.name = name;
        this.taxRate = taxRate;
        this.days = days;
        dateRange = new DateRange(startDate, endDate);
    }

    public FixedDailyRate(long budgetId) {
        this.budgetId = budgetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixedDailyRate)) return false;
        FixedDailyRate that = (FixedDailyRate) o;
        if (id != that.id) return false;
        if (budgetId != that.budgetId) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (taxRate != null ? taxRate.compareTo(that.taxRate) != 0 : that.taxRate != null) return false;
        if (days != that.days) return false;
        if (moneyAmount != null ? !moneyAmount.equals(that.moneyAmount) : that.moneyAmount != null) return false;
        if (dateRange != null ? !dateRange.equals(that.dateRange) : that.dateRange != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (budgetId ^ (budgetId >>> 32));
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (taxRate != null ? taxRate.hashCode() : 0);
        result = 31 * result + (days ^ (days >>> 32));
        result = 31 * result + (moneyAmount != null ? moneyAmount.hashCode() : 0);
        result = 31 * result + (dateRange != null ? dateRange.hashCode() : 0);
        return result;
    }
}
