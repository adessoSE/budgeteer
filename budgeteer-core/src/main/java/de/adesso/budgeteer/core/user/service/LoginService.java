package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.LoginUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUserByNameAndPasswordPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

  private final PasswordHasher passwordHasher;
  private final GetUserByNameAndPasswordPort getUserByNameAndPasswordPort;

  @Override
  public User login(String username, String password) throws InvalidLoginCredentialsException {
    return getUserByNameAndPasswordPort
        .getUserByNameAndPassword(username, passwordHasher.hash(password))
        .orElseThrow(InvalidLoginCredentialsException::new);
  }
}
