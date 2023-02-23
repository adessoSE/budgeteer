package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.core.exception.NotFoundException;

public interface AddUserToProjectUseCase {
  void addUserToProject(long userId, long projectId) throws NotFoundException;
}
