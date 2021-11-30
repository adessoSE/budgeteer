package de.adesso.budgeteer.rest.budget.model;

import lombok.Data;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateBudgetModel {
    @Positive
    long contractId;

    @NotEmpty
    String name;

    String description;

    @NotEmpty
    String importKey;

    @NotNull
    Money total;

    @NotNull
    Money limit;

    List<String> tags = new ArrayList<>();
}
