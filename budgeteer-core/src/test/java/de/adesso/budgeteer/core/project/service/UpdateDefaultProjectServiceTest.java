package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.out.ProjectExistsWithIdPort;
import de.adesso.budgeteer.core.project.port.out.UpdateDefaultProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateDefaultProjectServiceTest {

    @InjectMocks private UpdateDefaultProjectService updateDefaultProjectService;
    @Mock private UpdateDefaultProjectPort updateDefaultProjectPort;
    @Mock private ProjectExistsWithIdPort projectExistsWithIdPort;

    @Test
    void shouldUpdateDefaultProject() {
        var userId = 1L;
        var projectId = 2L;
        var expected = new Project(projectId, "name");
        when(updateDefaultProjectPort.updateDefaultProject(userId, projectId)).thenReturn(expected);
        when(projectExistsWithIdPort.projectExistsWithId(projectId)).thenReturn(true);

        var returned = updateDefaultProjectService.updateDefaultProject(userId, projectId);

        assertThat(returned).contains(expected);
    }

    @Test
    void shouldReturnNothingWhenDefaultProjectDoesNotExist() {
        var userId = 1L;
        var projectId = 2L;
        when(projectExistsWithIdPort.projectExistsWithId(projectId)).thenReturn(false);

        var returned = updateDefaultProjectService.updateDefaultProject(userId, projectId);

        assertThat(returned).isEmpty();
    }
}
