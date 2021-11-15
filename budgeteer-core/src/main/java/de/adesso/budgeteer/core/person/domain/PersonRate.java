package de.adesso.budgeteer.core.person.domain;

import de.adesso.budgeteer.common.date.DateRange;
import lombok.Value;
import org.joda.money.Money;

@Value
public class PersonRate {
    Money rate;
    Long budgetId;
    DateRange dateRange;
}
