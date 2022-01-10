package de.adesso.budgeteer.rest.invoice.model;

import lombok.Getter;

@Getter
public class InvoiceIdModel {
  private final long value;

  public InvoiceIdModel(String value) {
    this.value = Long.parseLong(value);
  }
}
