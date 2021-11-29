package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.in.UserHasAccessToProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.UserHasAccessToProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHasAccessToProjectService implements UserHasAccessToProjectUseCase {

    private final UserHasAccessToProjectPort userHasAccessToProjectPort;

    @Override
    public boolean userHasAccessToProject(String username, long projectId) {
        return userHasAccessToProjectPort.userHasAccessToProject(username, projectId);
    }
}
