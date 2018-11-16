package org.wickedsource.budgeteer.service.manualRecord;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecordEntity;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
public class ManualRecord implements Serializable {
    private long id;
    private long budgetId;
    private String description;
    private Money moneyAmount;
    private Date creationDate;
    private Date billingDate;

    public ManualRecord(long budgetId) {
        this.budgetId = budgetId;
    }

    public ManualRecord(ManualRecordEntity entity)
    {
        id = entity.getId();
        budgetId = entity.getBudget().getId();
        description = entity.getDescription();
        moneyAmount = entity.getMoneyAmount();
        creationDate = entity.getCreationDate();
        billingDate = entity.getBillingDate();
    }
}
