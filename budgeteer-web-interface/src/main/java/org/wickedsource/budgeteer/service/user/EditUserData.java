package org.wickedsource.budgeteer.service.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class EditUserData implements Serializable {

    private long id;
    private String name;
    private String mail;
    private String password;
    private String newPassword;
    private String newPasswordConfirmation;

    public EditUserData(long userId) {
        this.id = userId;
    }

    public EditUserData(long id, String name, String mail, String password, String newPassword, String newPasswordConfirmation) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.newPassword = newPassword;
        this.newPasswordConfirmation = newPasswordConfirmation;
    }
}
