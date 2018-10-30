package org.wickedsource.budgeteer.persistence.record;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class AddManualRecordData implements Serializable {
    private long id;
    private long projectId;
    private long budgetId;
    private String description;
    private Money MoneyAmount;


    public AddManualRecordData(long projectId, long budgetId) {
        this.projectId = projectId;
        this.budgetId = budgetId;
    }
}
