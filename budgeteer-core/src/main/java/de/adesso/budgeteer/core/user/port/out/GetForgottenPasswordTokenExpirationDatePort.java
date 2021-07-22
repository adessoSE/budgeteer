package de.adesso.budgeteer.core.user.port.out;

import java.time.LocalDateTime;

public interface GetForgottenPasswordTokenExpirationDatePort {
    LocalDateTime getForgottenPasswordTokenExpirationDate(String token);
}
