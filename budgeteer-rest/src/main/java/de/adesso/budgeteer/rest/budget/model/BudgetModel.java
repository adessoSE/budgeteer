package de.adesso.budgeteer.rest.budget.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.money.Money;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class BudgetModel {
    long id;
    Long contractId;
    String name;
    String contractName;
    String description;
    String importKey;
    Money total;
    Money spent;
    Money remaining;
    Money averageDailyRate;
    Money unplanned;
    Money limit;
    Date lastUpdated;
    List<String> tags;
}
