package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.out.UserHasAccessToProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHasAccessToProjectServiceTest {
    @Mock private UserHasAccessToProjectPort userHasAccessToProjectPort;
    @InjectMocks private UserHasAccessToProjectService userHasAccessToProjectService;

    @Test
    void shouldReturnTrueIfUserHasAccessToProject() {
        var username = "name";
        var projectId = 2L;
        when(userHasAccessToProjectPort.userHasAccessToProject(username, projectId)).thenReturn(true);

        var returned = userHasAccessToProjectService.userHasAccessToProject(username, projectId);

        assertThat(returned).isTrue();
    }

    @Test
    void shouldReturnFalseIfUserDoesNotHaveAccessToProject() {
        var username = "unauthorized-name";
        var projectId = 2L;
        when(userHasAccessToProjectPort.userHasAccessToProject(username, projectId)).thenReturn(false);

        var returned = userHasAccessToProjectService.userHasAccessToProject(username, projectId);

        assertThat(returned).isFalse();
    }
}