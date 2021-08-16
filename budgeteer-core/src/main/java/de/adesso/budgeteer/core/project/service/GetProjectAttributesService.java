package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.in.GetProjectAttributesUseCase;
import de.adesso.budgeteer.core.project.port.out.GetProjectAttributesPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProjectAttributesService implements GetProjectAttributesUseCase {

    private final GetProjectAttributesPort getProjectAttributesPort;

    @Override
    public List<String> getProjectAttributes(long projectId) {
        return getProjectAttributesPort.getProjectAttributes(projectId);
    }
}
