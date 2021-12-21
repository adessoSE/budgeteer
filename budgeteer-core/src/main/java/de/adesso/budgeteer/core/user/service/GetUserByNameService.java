package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.GetUserByNameUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUserByNamePort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByNameService implements GetUserByNameUseCase {

  private final GetUserByNamePort getUserByNamePort;

  @Override
  public Optional<User> getUserByName(String username) {
    return getUserByNamePort.getUserByName(username);
  }
}
