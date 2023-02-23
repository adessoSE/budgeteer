package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.exception.NotFoundException;
import de.adesso.budgeteer.core.project.port.in.RemoveUserFromProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.RemoveUserFromProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveUserFromProjectService implements RemoveUserFromProjectUseCase {

  private final RemoveUserFromProjectPort removeUserFromProjectPort;

  @Override
  public void removeUserFromProject(long userId, long projectId) throws NotFoundException {
    removeUserFromProjectPort.removeUserFromProject(userId, projectId);
  }
}
