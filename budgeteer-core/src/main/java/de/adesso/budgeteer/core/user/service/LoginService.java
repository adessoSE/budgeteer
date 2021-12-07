package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.LoginUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUserByNameOrEmailAndPasswordPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final PasswordHasher passwordHasher;
    private final GetUserByNameOrEmailAndPasswordPort getUserByNameOrEmailAndPasswordPort;

    @Override
    public User login(String usernameOrEmail, String password) throws InvalidLoginCredentialsException {
        return getUserByNameOrEmailAndPasswordPort.getUserByNameOrEmailAndPassword(usernameOrEmail, passwordHasher.hash(password))
                .orElseThrow(InvalidLoginCredentialsException::new);
    }
}
