package de.adesso.budgeteer.core.project.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import de.adesso.budgeteer.core.exception.NotFoundException;
import de.adesso.budgeteer.core.project.port.out.AddUserToProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddUserToProjectServiceTest {

  @InjectMocks private AddUserToProjectService addUserToProjectService;
  @Mock private AddUserToProjectPort addUserToProjectPort;

  @Test
  void shouldAddUserToProject() throws NotFoundException {
    var userId = 1L;
    var projectId = 2L;
    doNothing().when(addUserToProjectPort).addUserToProject(userId, projectId);

    addUserToProjectService.addUserToProject(userId, projectId);

    verify(addUserToProjectPort).addUserToProject(userId, projectId);
  }
}
