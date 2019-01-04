package org.wickedsource.budgeteer.web.pages.user.login;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginCredentials implements Serializable {

    private String username;

    private String password;
}
