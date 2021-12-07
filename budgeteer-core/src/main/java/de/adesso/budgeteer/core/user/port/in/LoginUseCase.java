package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.domain.User;

public interface LoginUseCase {
    User login(String username, String password) throws InvalidLoginCredentialsException;
}
