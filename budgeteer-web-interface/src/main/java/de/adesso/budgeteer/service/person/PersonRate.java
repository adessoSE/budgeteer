package de.adesso.budgeteer.service.person;

import de.adesso.budgeteer.service.DateRange;
import de.adesso.budgeteer.service.budget.BudgetBaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.money.Money;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRate implements Serializable {

    private Money rate;
    private BudgetBaseData budget;
    private DateRange dateRange;

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public void reset(){
        rate = null;
        budget = null;
        dateRange = null;
    }

}
