package de.adesso.budgeteer.rest.user.model;

import lombok.Getter;

@Getter
public class UserIdModel {
  private final long value;

  public UserIdModel(String value) {
    this.value = Long.parseLong(value);
  }
}
