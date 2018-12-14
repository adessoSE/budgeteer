package de.adesso.budgeteer.service.user;

import de.adesso.budgeteer.service.project.ProjectBaseData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
public class EditUserData implements Serializable {

    private long id;
    private String name;
    private String mail;
    private String password;
    private String newPassword;
    private String newPasswordConfirmation;
    private ProjectBaseData defaultProject;
    private Date lastLogin;

    public EditUserData(long userId) {
        this.id = userId;
    }

    public EditUserData(long id, String name, String mail, String password, String newPassword, String newPasswordConfirmation, ProjectBaseData defaultProject, Date lastLogin) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.newPassword = newPassword;
        this.newPasswordConfirmation = newPasswordConfirmation;
        this.defaultProject = defaultProject;
        this.lastLogin = lastLogin;
    }
}
