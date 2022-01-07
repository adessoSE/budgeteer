package de.adesso.budgeteer.rest.person.model;

import lombok.Getter;

@Getter
public class PersonIdModel {
  private final long value;

  public PersonIdModel(String value) {
    this.value = Long.parseLong(value);
  }
}
