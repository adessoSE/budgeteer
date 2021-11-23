package de.adesso.budgeteer.rest.user.model;

import lombok.Data;

@Data
public class RegisterModel {
    private String username;
    private String email;
    private String password;
}
