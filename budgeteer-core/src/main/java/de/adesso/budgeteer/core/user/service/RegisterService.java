package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.MailAlreadyInUseException;
import de.adesso.budgeteer.core.user.OnEmailChangedEvent;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.UsernameAlreadyInUseException;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import de.adesso.budgeteer.core.user.port.out.CreateUserPort;
import de.adesso.budgeteer.core.user.port.out.UserWithEmailExistsPort;
import de.adesso.budgeteer.core.user.port.out.UserWithNameExistsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

    private final UserWithNameExistsPort userWithNameExistsPort;
    private final UserWithEmailExistsPort userWithEmailExistsPort;
    private final CreateUserPort createUserPort;
    private final PasswordHasher passwordHasher;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void register(RegisterCommand command) throws UsernameAlreadyInUseException, MailAlreadyInUseException {
        if (userWithNameExistsPort.userWithNameExists(command.getUsername())) {
            throw new UsernameAlreadyInUseException();
        }
        if (!command.getMail().isBlank() && userWithEmailExistsPort.userWithEmailExists(command.getMail())) {
            throw new MailAlreadyInUseException();
        }
        var userId = createUserPort.createUser(command.getUsername(), command.getMail(), passwordHasher.hash(command.getPassword()));
        eventPublisher.publishEvent(new OnEmailChangedEvent(userId, command.getUsername(), command.getMail()));
    }
}
