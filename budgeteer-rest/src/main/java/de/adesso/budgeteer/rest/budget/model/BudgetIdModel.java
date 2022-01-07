package de.adesso.budgeteer.rest.budget.model;

import lombok.Getter;

@Getter
public class BudgetIdModel {
  private final long value;

  public BudgetIdModel(String value) {
    this.value = Long.parseLong(value);
  }
}
