package de.adesso.budgeteer.core.user.port.out;

public interface ChangeForgottenPasswordPort {
    void changeForgottenPassword(String token, String password);
}
