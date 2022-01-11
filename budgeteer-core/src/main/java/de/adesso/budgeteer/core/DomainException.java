package de.adesso.budgeteer.core;

import java.util.Collection;

public interface DomainException<T> {
  default void addCause(T t) {
    getCauses().add(t);
  }

  default boolean hasCause() {
    return !getCauses().isEmpty();
  }

  default boolean contains(T cause) {
    return getCauses().contains(cause);
  }

  Collection<T> getCauses();
}
