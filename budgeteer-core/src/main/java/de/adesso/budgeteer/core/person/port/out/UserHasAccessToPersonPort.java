package de.adesso.budgeteer.core.person.port.out;

public interface UserHasAccessToPersonPort {
    boolean userHasAccessToPerson(String username, long personId);
}
