package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.OnEmailChangedEvent;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.UserException;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import de.adesso.budgeteer.core.user.port.out.CreateUserPort;
import de.adesso.budgeteer.core.user.port.out.UserWithEmailExistsPort;
import de.adesso.budgeteer.core.user.port.out.UserWithNameExistsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

  private final UserWithNameExistsPort userWithNameExistsPort;
  private final UserWithEmailExistsPort userWithEmailExistsPort;
  private final CreateUserPort createUserPort;
  private final PasswordHasher passwordHasher;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void register(RegisterCommand command) throws UserException {
    var userException = new UserException();

    if (userWithNameExistsPort.userWithNameExists(command.getUsername())) {
      userException.addCause(UserException.UserErrors.USERNAME_ALREADY_IN_USE);
    }
    if (!command.getMail().isBlank()
        && userWithEmailExistsPort.userWithEmailExists(command.getMail())) {
      userException.addCause(UserException.UserErrors.MAIL_ALREADY_IN_USE);
    }

    if (userException.hasCause()) {
      throw userException;
    }

    var userId =
        createUserPort.createUser(
            command.getUsername(), command.getMail(), passwordHasher.hash(command.getPassword()));
    eventPublisher.publishEvent(
        new OnEmailChangedEvent(userId, command.getUsername(), command.getMail()));
  }
}
