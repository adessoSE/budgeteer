package org.wickedsource.budgeteer.service.fixedDailyRate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateEntity;
import org.wickedsource.budgeteer.service.DateRange;

import java.io.Serializable;
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

    public FixedDailyRate(FixedDailyRateEntity entity) {
        id = entity.getId();
        budgetId = entity.getBudget().getId();
        moneyAmount = entity.getMoneyAmount();
        startDate = entity.getStartDate();
        endDate = entity.getEndDate();
        description = entity.getDescription();
        name = entity.getName();
        dateRange = new DateRange(entity.getStartDate(), entity.getEndDate());
    }

    public FixedDailyRate(long budgetId) {
        this.budgetId = budgetId;
    }
}
