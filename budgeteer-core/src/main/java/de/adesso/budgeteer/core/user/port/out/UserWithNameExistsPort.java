package de.adesso.budgeteer.core.user.port.out;

public interface UserWithNameExistsPort {
    boolean userWithNameExists(String name);
}
