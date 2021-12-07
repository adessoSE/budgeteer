package de.adesso.budgeteer.core.user.port.out;

public interface CreateUserPort {
    long createUser(String username, String email, String password);
}
