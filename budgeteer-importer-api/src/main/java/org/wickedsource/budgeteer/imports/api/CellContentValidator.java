package org.wickedsource.budgeteer.imports.api;

@FunctionalInterface
public interface CellContentValidator {
    boolean isValid(String content);
}
