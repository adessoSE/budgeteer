package de.adesso.budgeteer.rest.user;

import de.adesso.budgeteer.core.user.MailAlreadyInUseException;
import de.adesso.budgeteer.core.user.UserException;
import de.adesso.budgeteer.core.user.UsernameAlreadyInUseException;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import de.adesso.budgeteer.rest.user.exceptions.RegisterUserException;
import de.adesso.budgeteer.rest.user.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final RegisterUseCase registerUseCase;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterModel registerModel) {
        try {
            registerUseCase.register(new RegisterUseCase.RegisterCommand(registerModel.getUsername(), registerModel.getEmail(), passwordEncoder.encode(registerModel.getPassword())));
        } catch (UserException e) {
            throw new RegisterUserException(e);
        }
    }
}
