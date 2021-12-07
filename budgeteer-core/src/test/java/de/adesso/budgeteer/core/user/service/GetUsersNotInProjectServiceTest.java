package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.out.GetUsersNotInProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUsersNotInProjectServiceTest {

    @InjectMocks private GetUsersNotInProjectService getUsersNotInProjectService;
    @Mock private GetUsersNotInProjectPort getUsersNotInProjectPort;

    @Test
    void shouldReturnUsersNotInProject() {
        var projectId = 1540L;
        var expectedUsers = List.of(new User(1L, "test1"), new User(2L, "test2"));
        when(getUsersNotInProjectPort.getUsersNotInProject(projectId)).thenReturn(expectedUsers);

        var returnedUsers = getUsersNotInProjectService.getUsersNotInProject(projectId);

        assertThat(returnedUsers).isEqualTo(expectedUsers);
    }
}
