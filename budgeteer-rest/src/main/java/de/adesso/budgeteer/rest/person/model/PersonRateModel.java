package de.adesso.budgeteer.rest.person.model;

import de.adesso.budgeteer.common.date.DateRange;
import lombok.Value;
import org.joda.money.Money;

@Value
public class PersonRateModel {
    Money rate;
    Long budgetId;
    DateRange dateRange;
}
