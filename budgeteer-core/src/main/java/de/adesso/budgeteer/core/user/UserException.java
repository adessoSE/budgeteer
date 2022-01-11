package de.adesso.budgeteer.core.user;

import de.adesso.budgeteer.core.DomainException;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class UserException extends Exception implements DomainException<UserException.UserErrors> {
  private final Set<UserErrors> causes = new HashSet<>();

  public enum UserErrors {
    USERNAME_ALREADY_IN_USE,
    MAIL_ALREADY_IN_USE
  }
}
