package de.adesso.budgeteer.rest.project.model;

import lombok.Data;

@Data
public class ProjectIdModel {
  private final long value;

  public ProjectIdModel(String value) {
    this.value = Long.parseLong(value);
  }
}
