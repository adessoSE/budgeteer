package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.*;
import de.adesso.budgeteer.core.user.port.in.UpdateUserUseCase;
import de.adesso.budgeteer.core.user.port.out.*;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserUseCase {

  private final UserWithNameExistsPort userWithNameExistsPort;
  private final UserWithEmailExistsPort userWithEmailExistsPort;
  private final GetUserWithEmailPort getUserWithEmailPort;
  private final PasswordMatchesPort passwordMatchesPort;
  private final ApplicationEventPublisher eventPublisher;
  private final UpdateUserPort updateUserPort;
  private final PasswordHasher passwordHasher;

  @Override
  public void updateUser(UpdateUserCommand command)
      throws UsernameAlreadyInUseException, MailAlreadyInUseException,
          InvalidLoginCredentialsException {
    Objects.requireNonNull(command.getName());
    Objects.requireNonNull(command.getEmail());
    var user = getUserWithEmailPort.getUserWithEmail(command.getId());
    if (!user.getName().equals(command.getName())
        && userWithNameExistsPort.userWithNameExists(command.getName())) {
      throw new UsernameAlreadyInUseException();
    }
    if (!user.getEmail().equals(command.getEmail())
        && userWithEmailExistsPort.userWithEmailExists(command.getEmail())) {
      throw new MailAlreadyInUseException();
    }
    var password = changedPassword(command) ? command.getNewPassword() : null;
    updateUserPort.updateUser(command.getId(), command.getName(), command.getEmail(), password);
    if (!Objects.equals(user.getEmail(), command.getEmail())) {
      eventPublisher.publishEvent(
          new OnEmailChangedEvent(command.getId(), command.getName(), command.getEmail()));
    }
  }

  private boolean changedPassword(UpdateUserCommand command)
      throws InvalidLoginCredentialsException {
    if (command.getNewPassword() == null) {
      return false;
    }
    if (command.getCurrentPassword() == null) {
      throw new InvalidLoginCredentialsException();
    }
    if (!passwordMatchesPort.passwordMatches(command.getId(), command.getCurrentPassword())) {
      throw new InvalidLoginCredentialsException();
    }
    return true;
  }
}
