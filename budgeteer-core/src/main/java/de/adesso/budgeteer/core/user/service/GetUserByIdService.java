package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.GetUserByIdUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUserByIdPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdService implements GetUserByIdUseCase {

  private final GetUserByIdPort getUserByIdPort;

  @Override
  public Optional<User> getUserById(long id) {
    return getUserByIdPort.getUserById(id);
  }
}
