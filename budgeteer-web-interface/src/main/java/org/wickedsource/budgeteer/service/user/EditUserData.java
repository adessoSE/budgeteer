package org.wickedsource.budgeteer.service.user;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EditUserData implements Serializable {

  private long id;
  private String name;
  private String password;
  private String newPassword;
  private String newPasswordConfirmation;

  public EditUserData(long userId) {
    this.id = userId;
  }

  public EditUserData(
      long id, String name, String password, String newPassword, String newPasswordConfirmation) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.newPassword = newPassword;
    this.newPasswordConfirmation = newPasswordConfirmation;
  }
}
