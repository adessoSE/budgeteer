package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.out.GetUserByEmailPort;
import de.adesso.budgeteer.core.user.port.out.GetUsersInProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUsersInProjectServiceTest {

    @InjectMocks private GetUsersInProjectService getUsersInProjectService;
    @Mock private GetUsersInProjectPort getUsersInProjectPort;

    @Test
    void shouldReturnListOfUsersInProject() {
        long projectId = 1534L;
        var expectedUsers = List.of(new User(1, "test1"), new User(2, "test2"));
        when(getUsersInProjectPort.getUsersInProject(projectId)).thenReturn(expectedUsers);

        var returnedUsers = getUsersInProjectService.getUsersInProject(projectId);

        assertThat(returnedUsers).isEqualTo(expectedUsers);
    }
}
