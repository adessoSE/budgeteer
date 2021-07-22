package de.adesso.budgeteer.core.user.port.out;

import java.time.LocalDateTime;

public interface CreateVerificationTokenPort {
    void createVerificationToken(long userId, String token, LocalDateTime expirationDate);
}
