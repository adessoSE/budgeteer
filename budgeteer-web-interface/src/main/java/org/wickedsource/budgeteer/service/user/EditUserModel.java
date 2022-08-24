package org.wickedsource.budgeteer.service.user;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EditUserModel implements Serializable {

  private long id;
  private String name;
  private String mail;
  private String password;
  private String newPassword;
  private String newPasswordConfirmation;

  public EditUserModel(long id, String name, String mail) {
    this.id = id;
    this.name = name;
    this.mail = mail;
  }
}
