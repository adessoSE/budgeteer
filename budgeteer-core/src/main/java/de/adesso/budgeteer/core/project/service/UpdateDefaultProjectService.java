package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.in.UpdateDefaultProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.UpdateDefaultProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDefaultProjectService implements UpdateDefaultProjectUseCase {

    private final UpdateDefaultProjectPort updateDefaultProjectPort;

    @Override
    public void updateDefaultProject(long userId, long projectId) {
        updateDefaultProjectPort.updateDefaultProject(userId, projectId);
    }
}
