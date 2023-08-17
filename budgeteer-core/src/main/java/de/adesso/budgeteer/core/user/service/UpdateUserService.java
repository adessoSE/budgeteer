package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.UsernameAlreadyInUseException;
import de.adesso.budgeteer.core.user.port.in.UpdateUserUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUserByIdPort;
import de.adesso.budgeteer.core.user.port.out.PasswordMatchesPort;
import de.adesso.budgeteer.core.user.port.out.UpdateUserPort;
import de.adesso.budgeteer.core.user.port.out.UserWithNameExistsPort;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserUseCase {

  private final UserWithNameExistsPort userWithNameExistsPort;
  private final GetUserByIdPort getUserByIdPort;
  private final PasswordMatchesPort passwordMatchesPort;
  private final UpdateUserPort updateUserPort;
  private final PasswordHasher passwordHasher;

  @Override
  public void updateUser(UpdateUserCommand command)
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException {
    Objects.requireNonNull(command.getName());
    var user = getUserByIdPort.getUserById(command.getId()).orElseThrow();
    if (!user.getName().equals(command.getName())
        && userWithNameExistsPort.userWithNameExists(command.getName())) {
      throw new UsernameAlreadyInUseException();
    }
    var passwordHash =
        changedPassword(command) ? passwordHasher.hash(command.getNewPassword()) : null;
    updateUserPort.updateUser(command.getId(), command.getName(), passwordHash);
  }

  private boolean changedPassword(UpdateUserCommand command)
      throws InvalidLoginCredentialsException {
    if (command.getNewPassword() == null) {
      return false;
    }
    if (command.getCurrentPassword() == null) {
      throw new InvalidLoginCredentialsException();
    }
    if (!passwordMatchesPort.passwordMatches(
        command.getId(), passwordHasher.hash(command.getCurrentPassword()))) {
      throw new InvalidLoginCredentialsException();
    }
    return true;
  }
}
