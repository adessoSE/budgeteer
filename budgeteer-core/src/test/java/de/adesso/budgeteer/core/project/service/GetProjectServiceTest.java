package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.out.GetProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProjectServiceTest {

    @InjectMocks private GetProjectService getProjectService;
    @Mock private GetProjectPort getProjectPort;

    @Test
    void shouldReturnProject() {
        var expectedProject = new Project(1L, "name");
        when(getProjectPort.getProject(expectedProject.getId())).thenReturn(expectedProject);

        var returnedProject = getProjectService.getProject(expectedProject.getId());

        assertThat(returnedProject).isEqualTo(expectedProject);
    }

}
