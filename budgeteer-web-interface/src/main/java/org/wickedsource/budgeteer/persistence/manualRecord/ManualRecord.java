package org.wickedsource.budgeteer.persistence.manualRecord;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
public class ManualRecord implements Serializable {
    private long id;
    private long budgetId;
    private String description;
    private Money moneyAmount;
    private Date date;

    public ManualRecord(long budgetId) {
        this.budgetId = budgetId;
    }

    public ManualRecord(ManualRecordEntity entity)
    {
        id = entity.getId();
        budgetId = entity.getBudget().getId();
        description = entity.getDescription();
        moneyAmount = MoneyUtil.createMoneyFromCents(entity.getCents());
        date = entity.getDate();
    }
}
