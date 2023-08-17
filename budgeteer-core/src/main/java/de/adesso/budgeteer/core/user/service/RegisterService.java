package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.UserException;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import de.adesso.budgeteer.core.user.port.out.CreateUserPort;
import de.adesso.budgeteer.core.user.port.out.UserWithNameExistsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

  private final UserWithNameExistsPort userWithNameExistsPort;
  private final CreateUserPort createUserPort;

  @Override
  public void register(RegisterCommand command) throws UserException {
    var userException = new UserException();

    if (userWithNameExistsPort.userWithNameExists(command.getUsername())) {
      userException.addCause(UserException.UserErrors.USERNAME_ALREADY_IN_USE);
    }
    if (userException.hasCause()) {
      throw userException;
    }

    createUserPort.createUser(command.getUsername(), command.getPassword());
  }
}
