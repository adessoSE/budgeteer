package org.wickedsource.budgeteer.web.pages.user.resettoken;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResetTokenModel implements Serializable {
  private final long userId;
  private final String mail;
}
