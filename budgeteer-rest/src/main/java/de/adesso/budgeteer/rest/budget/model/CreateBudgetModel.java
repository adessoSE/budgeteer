package de.adesso.budgeteer.rest.budget.model;

import lombok.Data;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateBudgetModel {
    @Positive
    long projectId;

    @Positive
    Long contractId;

    @NotEmpty
    String name;

    String description;

    @NotEmpty
    String importKey;

    @NotNull
    Money total;

    Money limit;

    List<String> tags = new ArrayList<>();
}
