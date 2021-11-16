package de.adesso.budgeteer.core.budget.domain;

import lombok.Value;
import org.joda.money.Money;

import java.util.Date;
import java.util.List;

@Value
public class Budget {
    long id;
    long contractId;
    String name;
    String contractName;
    String description;
    Money total;
    Money spent;
    Money remaining;
    Money averageDailyRate;
    Money unplanned;
    Money limit;
    Date lastUpdated;
    List<String> tags;
}
