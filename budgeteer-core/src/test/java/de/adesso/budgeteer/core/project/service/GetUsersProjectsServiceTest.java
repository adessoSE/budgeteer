package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.out.GetUsersProjectsPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUsersProjectsServiceTest {

    @InjectMocks private GetUsersProjectsService getUsersProjectsService;
    @Mock private GetUsersProjectsPort getUsersProjectsPort;

    @Test
    void shouldReturnUsersProjectsIfUserHasProjects() {
        var userId = 3L;
        var expectedProjects = List.of(new Project(1L, "name1"), new Project(2L, "name2"));
        when(getUsersProjectsPort.getUsersProjects(userId)).thenReturn(expectedProjects);

        var returnedProjects = getUsersProjectsService.getUsersProjects(userId);

        assertThat(returnedProjects).isEqualTo(expectedProjects);
    }

    @Test
    void shouldReturnNoProjectsIfUserHasNoProjects() {
        var userId = 3L;
        when(getUsersProjectsPort.getUsersProjects(userId)).thenReturn(Collections.emptyList());

        var returnedProjects = getUsersProjectsService.getUsersProjects(userId);

        assertThat(returnedProjects).isEmpty();
    }
}
