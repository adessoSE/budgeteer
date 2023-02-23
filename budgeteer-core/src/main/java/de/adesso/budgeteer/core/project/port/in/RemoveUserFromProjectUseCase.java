package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.core.exception.NotFoundException;

public interface RemoveUserFromProjectUseCase {
  void removeUserFromProject(long userId, long projectId) throws NotFoundException;
}
