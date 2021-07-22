package de.adesso.budgeteer.core.user.port.out;

public interface VerificationTokenExistsPort {
    boolean verificationTokenExists(String token);
}
