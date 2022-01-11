package de.adesso.budgeteer.rest.user;

import de.adesso.budgeteer.core.user.UserException;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.GetUserByNameUseCase;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import de.adesso.budgeteer.rest.user.errors.RegisterUserError;
import de.adesso.budgeteer.rest.user.exceptions.RegisterUserException;
import de.adesso.budgeteer.rest.user.model.RegisterModel;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

  @ExceptionHandler(RegisterUserException.class)
  public ResponseEntity<RegisterUserError> handleRegisterUserException(
      RegisterUserException registerUserException) {
    var causes = registerUserException.getUserException().getCauses();

    var registerUserError = new RegisterUserError();
    registerUserError.setUsernameAlreadyInUse(
        causes.contains(UserException.UserErrors.USERNAME_ALREADY_IN_USE));
    registerUserError.setMailAlreadyInUse(
        causes.contains(UserException.UserErrors.MAIL_ALREADY_IN_USE));

    return ResponseEntity.badRequest().body(registerUserError);
  }
}
