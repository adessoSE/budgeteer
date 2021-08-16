package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.in.RemoveUserFromProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveUserFromProjectServiceTest {

    @InjectMocks private RemoveUserFromProjectService removeUserFromProjectService;
    @Mock private RemoveUserFromProjectPort removeUserFromProjectPort;

    @Test
    void shouldRemoveUserFromProject() {
        var userId = 1L;
        var projectId = 2L;
        doNothing().when(removeUserFromProjectPort).removeUserFromProject(userId, projectId);

        removeUserFromProjectService.removeUserFromProject(userId, projectId);

        verify(removeUserFromProjectPort).removeUserFromProject(userId, projectId);
    }
}
