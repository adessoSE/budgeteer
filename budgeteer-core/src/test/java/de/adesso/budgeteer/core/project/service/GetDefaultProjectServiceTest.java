package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.out.GetDefaultProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetDefaultProjectServiceTest {

    @InjectMocks private GetDefaultProjectService getDefaultProjectService;
    @Mock private GetDefaultProjectPort getDefaultProjectPort;

    @Test
    void shouldReturnEmptyOptionalIfUserHasNoDefaultProject() {
        var userId = 1L;
        when(getDefaultProjectPort.getDefaultProject(userId)).thenReturn(Optional.empty());

        var returnedProject = getDefaultProjectService.getDefaultProject(userId);

        assertThat(returnedProject).isEmpty();
    }

    @Test
    void shouldReturnDefaultProjectIfUserHasDefaultProject() {
        var userId = 1L;
        var expectedProject = new Project(1L, "name");
        when(getDefaultProjectPort.getDefaultProject(userId)).thenReturn(Optional.of(expectedProject));

        var returnedProject = getDefaultProjectService.getDefaultProject(userId);

        assertThat(returnedProject).contains(expectedProject);
    }
}
