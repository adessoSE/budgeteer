package de.adesso.budgeteer.rest.budget.model;

import lombok.Data;
import org.joda.money.Money;

import java.util.List;

@Data
public class UpdateBudgetModel {
    long contractId;
    String name;
    String description;
    String importKey;
    Money total;
    Money limit;
    List<String> tags;
}
