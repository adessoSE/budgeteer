package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.common.date.DateRange;
import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.UpdateProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.GetProjectPort;
import de.adesso.budgeteer.core.project.port.out.ProjectExistsWithNamePort;
import de.adesso.budgeteer.core.project.port.out.UpdateProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProjectServiceTest {

    @InjectMocks private UpdateProjectService updateProjectService;
    @Mock private UpdateProjectPort updateProjectPort;
    @Mock private ProjectExistsWithNamePort projectExistsWithNamePort;
    @Mock private GetProjectPort getProjectPort;

    @Test
    void shouldUpdateProjectIfEverythingIsValid() throws ProjectException {
        var command = new UpdateProjectUseCase.UpdateProjectCommand(
                1L,
                "new-name",
                new DateRange(LocalDate.now(), LocalDate.now().plusDays(5))
        );

        var expected = new Project(command.getId(), command.getName());

        when(projectExistsWithNamePort.projectExistsWithName(command.getName())).thenReturn(false);
        when(getProjectPort.getProject(command.getId())).thenReturn(new Project(command.getId(), "old-name"));
        when(updateProjectPort.updateProject(command.getId(), command.getName(), command.getDateRange())).thenReturn(expected);

        var returnedProject = updateProjectService.updateProject(command);

        assertThat(returnedProject).isEqualTo(expected);
    }

    @Test
    void shouldThrowProjectNameAlreadyInUseExceptionIfNewNameIsAlreadyInUseAndNotTheSameAsTheOld() {
        var command = new UpdateProjectUseCase.UpdateProjectCommand(1L, "new-name", new DateRange(LocalDate.now(), LocalDate.now().plusDays(5)));
        when(getProjectPort.getProject(command.getId())).thenReturn(new Project(command.getId(), "old-name"));
        when(projectExistsWithNamePort.projectExistsWithName(command.getName())).thenReturn(true);

        assertThatExceptionOfType(ProjectException.class)
                .isThrownBy(() -> updateProjectService.updateProject(command))
                .matches(e -> e.getCauses().contains(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE));
    }

    @Test
    void shouldNotThrowProjectNameAlreadyInUseExceptionIfNewProjectNameIsTheSameAsTheOldOne() throws ProjectException{
        var command = new UpdateProjectUseCase.UpdateProjectCommand(1L, "old-name", new DateRange(LocalDate.now(), LocalDate.now().plusDays(5)));
        when(getProjectPort.getProject(command.getId())).thenReturn(new Project(command.getId(), "old-name"));

        updateProjectService.updateProject(command);

        verify(updateProjectPort).updateProject(command.getId(), command.getName(), command.getDateRange());
    }

    @Test
    void shouldThrowProjectNotFoundExceptionIfProjectWithIdDoesNotExist() {
        var command = new UpdateProjectUseCase.UpdateProjectCommand(1L, "new-name", new DateRange(LocalDate.now(), LocalDate.now().plusDays(5)));
        when(getProjectPort.getProject(command.getId())).thenReturn(null);

        assertThatExceptionOfType(ProjectException.class)
                .isThrownBy(() -> updateProjectService.updateProject(command))
                .matches(e -> e.getCauses().contains(ProjectException.ProjectErrors.PROJECT_NOT_FOUND));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void shouldThrowIllegalArgumentExceptionIfNameIsNullOrBlank(String newName) {
        var command = new UpdateProjectUseCase.UpdateProjectCommand(1L, newName, new DateRange(LocalDate.now(), LocalDate.now().plusDays(5)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> updateProjectService.updateProject(command))
                .withMessage("name may not be null or blank");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfDateRangeIsNull() {
        var command = new UpdateProjectUseCase.UpdateProjectCommand(1L, "new-name", null);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> updateProjectService.updateProject(command))
                .withMessage("dateRange may not be null");
    }
}
