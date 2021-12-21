package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.domain.User;
import java.util.Optional;

public interface GetUserByNameUseCase {
  Optional<User> getUserByName(String username);
}
