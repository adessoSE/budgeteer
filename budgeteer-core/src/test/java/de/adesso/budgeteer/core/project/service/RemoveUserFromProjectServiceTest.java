package de.adesso.budgeteer.core.project.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import de.adesso.budgeteer.core.exception.NotFoundException;
import de.adesso.budgeteer.core.project.port.out.RemoveUserFromProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveUserFromProjectServiceTest {

  @InjectMocks private RemoveUserFromProjectService removeUserFromProjectService;
  @Mock private RemoveUserFromProjectPort removeUserFromProjectPort;

  @Test
  void shouldRemoveUserFromProject() throws NotFoundException {
    var userId = 1L;
    var projectId = 2L;
    doNothing().when(removeUserFromProjectPort).removeUserFromProject(userId, projectId);

    removeUserFromProjectService.removeUserFromProject(userId, projectId);

    verify(removeUserFromProjectPort).removeUserFromProject(userId, projectId);
  }
}
