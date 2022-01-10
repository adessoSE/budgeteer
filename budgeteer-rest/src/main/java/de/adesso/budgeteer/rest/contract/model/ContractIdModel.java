package de.adesso.budgeteer.rest.contract.model;

import lombok.Data;

@Data
public class ContractIdModel {
  private final long value;

  public ContractIdModel(String value) {
    this.value = Long.parseLong(value);
  }
}
