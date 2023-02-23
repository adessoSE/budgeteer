package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.core.exception.NotFoundException;

public interface RemoveUserFromProjectPort {
  void removeUserFromProject(long userId, long projectId) throws NotFoundException;
}
