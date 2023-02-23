package de.adesso.budgeteer.core.exception;

public class NotFoundException extends Exception {

  private final Class<?> clazz;

  public NotFoundException(Class<?> clazz) {
    this.clazz = clazz;
  }

  public Class<?> getClazz() {
    return clazz;
  }
}
