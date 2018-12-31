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
    }

    public FixedDailyRate(long budgetId) {
        this.budgetId = budgetId;
    }
}
