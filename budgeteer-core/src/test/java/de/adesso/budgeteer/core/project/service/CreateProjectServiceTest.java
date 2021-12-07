package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.core.project.port.in.CreateProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.CreateProjectPort;
import de.adesso.budgeteer.core.project.port.out.ProjectExistsWithNamePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProjectServiceTest {

    @InjectMocks private CreateProjectService createProjectService;
    @Mock private CreateProjectPort createProjectPort;
    @Mock private ProjectExistsWithNamePort projectExistsWithNamePort;

    @Test
    void shouldThrowProjectNameAlreadyInUseExceptionIfProjectWithNameAlreadyExists() {
        var existingProjectName = "name";
        when(projectExistsWithNamePort.projectExistsWithName(existingProjectName)).thenReturn(true);

        assertThatExceptionOfType(ProjectException.class)
                .isThrownBy(() -> createProjectService.createProject(new CreateProjectUseCase.CreateProjectCommand(existingProjectName, 1L)))
                .matches(e -> e.getCauses().contains(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE));
    }

    @Test
    void shouldCreateNewProjectWhenProjectWithNameDoesNotYetExist() throws ProjectException {
        var name = "name";
        var userId = 1L;
        var expectedProject = new Project(3L, name);
        when(projectExistsWithNamePort.projectExistsWithName(name)).thenReturn(false);
        when(createProjectPort.createProject(userId, name)).thenReturn(expectedProject);

        var returnedProject = createProjectService.createProject(new CreateProjectUseCase.CreateProjectCommand(name, userId));

        assertThat(returnedProject).isEqualTo(expectedProject);
    }
}
