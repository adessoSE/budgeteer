package de.adesso.budgeteer.core.user.domain;

import lombok.Value;

@Value
public class UserWithEmail {
    long id;
    String name;
    String email;
}
