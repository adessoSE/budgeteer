package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.out.DeleteProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteProjectServiceTest {

    @InjectMocks private DeleteProjectService deleteProjectService;
    @Mock private DeleteProjectPort deleteProjectPort;

    @Test
    void shouldDeleteProject() {
        var projectId = 1L;
        doNothing().when(deleteProjectPort).deleteProject(projectId);

        deleteProjectService.deleteProject(projectId);

        verify(deleteProjectPort).deleteProject(projectId);
    }
}
