package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.GetDefaultProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.GetDefaultProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetDefaultProjectService implements GetDefaultProjectUseCase {

    private final GetDefaultProjectPort getDefaultProjectPort;

    @Override
    public Optional<Project> getDefaultProject(long userId) {
        return getDefaultProjectPort.getDefaultProject(userId);
    }
}
