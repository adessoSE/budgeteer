package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.GetProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.GetProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProjectService implements GetProjectUseCase {

    private final GetProjectPort getProjectPort;

    @Override
    public Project getProject(long projectId) {
        return getProjectPort.getProject(projectId);
    }
}
