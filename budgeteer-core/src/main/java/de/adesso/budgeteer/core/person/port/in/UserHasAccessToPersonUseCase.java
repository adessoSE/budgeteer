package de.adesso.budgeteer.core.person.port.in;

public interface UserHasAccessToPersonUseCase {
    boolean userHasAccessToPerson(String username, long personId);
}
