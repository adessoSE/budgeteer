package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.core.exception.NotFoundException;

public interface AddUserToProjectPort {
  void addUserToProject(long userId, long projectId) throws NotFoundException;
}
