package de.adesso.budgeteer.rest.budget.model;

import lombok.Data;
import org.joda.money.Money;

import java.util.List;

@Data
public class CreateBudgetModel {
    long projectId;
    Long contractId;
    String name;
    String description;
    String importKey;
    Money total;
    Money limit;
    List<String> tags;
}
