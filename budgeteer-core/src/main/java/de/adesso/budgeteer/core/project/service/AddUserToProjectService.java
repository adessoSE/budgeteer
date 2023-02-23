package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.exception.NotFoundException;
import de.adesso.budgeteer.core.project.port.in.AddUserToProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.AddUserToProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddUserToProjectService implements AddUserToProjectUseCase {

  private final AddUserToProjectPort addUserToProjectPort;

  @Override
  public void addUserToProject(long userId, long projectId) throws NotFoundException {
    addUserToProjectPort.addUserToProject(userId, projectId);
  }
}
