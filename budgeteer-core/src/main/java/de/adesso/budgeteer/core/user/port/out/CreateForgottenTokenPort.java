package de.adesso.budgeteer.core.user.port.out;

import java.time.LocalDateTime;

public interface CreateForgottenTokenPort {
    void createForgottenPasswordToken(long userId, String token, LocalDateTime expirationDate);
}
