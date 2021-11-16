package de.adesso.budgeteer.core.security.domain;

import lombok.Value;

@Value
public class AuthenticationUser {
    long id;
    String username;
    String password;
}
