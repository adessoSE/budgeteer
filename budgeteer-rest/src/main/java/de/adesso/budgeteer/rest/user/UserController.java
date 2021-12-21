package de.adesso.budgeteer.rest.user;

import de.adesso.budgeteer.core.user.UserException;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.GetUserByNameUseCase;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import de.adesso.budgeteer.rest.user.exceptions.RegisterUserException;
import de.adesso.budgeteer.rest.user.model.RegisterModel;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final GetUserByNameUseCase getUserByNameUseCase;
  private final RegisterUseCase registerUseCase;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public void register(@RequestBody @Valid RegisterModel registerModel) {
    try {
      registerUseCase.register(
          new RegisterUseCase.RegisterCommand(
              registerModel.getUsername(),
              registerModel.getEmail(),
              passwordEncoder.encode(registerModel.getPassword())));
    } catch (UserException e) {
      throw new RegisterUserException(e);
    }
  }

  @GetMapping("users/self")
  public User getSelf(Principal principal) {
    return getUserByNameUseCase
        .getUserByName(principal.getName())
        .orElseThrow(IllegalArgumentException::new);
  }
}
