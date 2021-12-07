package de.adesso.budgeteer.core.user.port.out;

public interface ForgottenPasswordTokenExistsPort {
    boolean forgottenPasswordTokenExists(String token);
}
