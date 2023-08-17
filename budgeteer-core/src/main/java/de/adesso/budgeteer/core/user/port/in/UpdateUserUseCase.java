package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.UsernameAlreadyInUseException;
import lombok.Value;

public interface UpdateUserUseCase {
  void updateUser(UpdateUserCommand command)
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException;

  @Value
  class UpdateUserCommand {
    long id;
    String name;
    String currentPassword;
    String newPassword;
  }
}
