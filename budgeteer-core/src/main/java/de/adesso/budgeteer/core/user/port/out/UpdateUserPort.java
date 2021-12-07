package de.adesso.budgeteer.core.user.port.out;

public interface UpdateUserPort {
    void updateUser(long userId, String name, String email, String passwordHash);
}
