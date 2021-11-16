package de.adesso.budgeteer.rest.authentication.model;

import lombok.Value;

@Value
public class AuthenticationModel {
    String username;
    String password;
}
