package de.adesso.budgeteer.core.user.port.out;

public interface PasswordMatchesPort {
    boolean passwordMatches(long userId, String passwordHash);
}
