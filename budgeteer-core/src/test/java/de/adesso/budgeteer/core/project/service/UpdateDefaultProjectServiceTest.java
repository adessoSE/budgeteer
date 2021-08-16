package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.out.UpdateDefaultProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateDefaultProjectServiceTest {

    @InjectMocks private UpdateDefaultProjectService updateDefaultProjectService;
    @Mock private UpdateDefaultProjectPort updateDefaultProjectPort;

    @Test
    void shouldUpdateDefaultProject() {
        var userId = 1L;
        var projectId = 2L;
        doNothing().when(updateDefaultProjectPort).updateDefaultProject(userId, projectId);

        updateDefaultProjectService.updateDefaultProject(userId, projectId);

        verify(updateDefaultProjectPort).updateDefaultProject(userId, projectId);
    }
}
