package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.GetUsersInProjectUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUsersInProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUsersInProjectService implements GetUsersInProjectUseCase {

    private final GetUsersInProjectPort getUsersInProjectPort;

    @Override
    public List<User> getUsersInProject(long projectId) {
        return getUsersInProjectPort.getUsersInProject(projectId);
    }
}
