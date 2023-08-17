package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.UserException;
import lombok.Value;

public interface RegisterUseCase {
  void register(RegisterCommand command) throws UserException;

  @Value
  class RegisterCommand {
    String username;
    String password;
  }
}
