package de.adesso.budgeteer.core.user.port.out;

import de.adesso.budgeteer.core.user.domain.User;
import java.util.Optional;

public interface GetUserByNamePort {
  Optional<User> getUserByName(String name);
}
