package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.GetUsersProjectsUseCase;
import de.adesso.budgeteer.core.project.port.out.GetUsersProjectsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUsersProjectsService implements GetUsersProjectsUseCase {

    private final GetUsersProjectsPort getUsersProjectsPort;

    @Override
    public List<Project> getUsersProjects(long userId) {
        return getUsersProjectsPort.getUsersProjects(userId);
    }
}
