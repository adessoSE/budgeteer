package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.in.DeleteProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.DeleteProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProjectService implements DeleteProjectUseCase {

    private final DeleteProjectPort deleteProjectPort;

    @Override
    public void deleteProject(long projectId) {
        deleteProjectPort.deleteProject(projectId);
    }
}
