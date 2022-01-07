package de.adesso.budgeteer.rest.budget.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.joda.money.Money;

@Data
public class CreateBudgetModel {
  @Positive Long contractId;

  @NotEmpty String name;

  String description;

  @NotEmpty String importKey;

  @NotNull Money total;

  Money limit;

  List<String> tags = new ArrayList<>();
}
