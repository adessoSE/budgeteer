package de.adesso.budgeteer.core.user.port.out;

public interface VerifyEmailPort {
    void verifyEmail(String token);
}
