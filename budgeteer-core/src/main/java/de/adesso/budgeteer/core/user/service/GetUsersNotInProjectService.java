package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.GetUsersNotInProjectUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUsersNotInProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUsersNotInProjectService implements GetUsersNotInProjectUseCase {

    private final GetUsersNotInProjectPort getUsersNotInProjectPort;

    @Override
    public List<User> getUsersNotInProject(long projectId) {
        return getUsersNotInProjectPort.getUsersNotInProject(projectId);
    }
}
