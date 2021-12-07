package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.ProjectWithDate;
import de.adesso.budgeteer.core.project.port.in.GetProjectWithDateUseCase;
import de.adesso.budgeteer.core.project.port.out.GetProjectWithDatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProjectWithDateService implements GetProjectWithDateUseCase {

    private final GetProjectWithDatePort getProjectWithDatePort;

    @Override
    public ProjectWithDate getProjectWithDate(long projectId) {
        return getProjectWithDatePort.getProjectWithDate(projectId);
    }
}
