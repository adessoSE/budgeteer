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
}
