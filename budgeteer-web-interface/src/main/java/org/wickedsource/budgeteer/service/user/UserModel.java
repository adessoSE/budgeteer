package org.wickedsource.budgeteer.service.user;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserModel implements Serializable {
  private long id;
  private String name;
}
