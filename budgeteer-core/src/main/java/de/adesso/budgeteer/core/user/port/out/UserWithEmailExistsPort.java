package de.adesso.budgeteer.core.user.port.out;

public interface UserWithEmailExistsPort {
    boolean userWithEmailExists(String email);
}
