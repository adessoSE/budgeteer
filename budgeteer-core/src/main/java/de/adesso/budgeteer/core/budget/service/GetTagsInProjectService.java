package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.in.GetTagsInProjectUseCase;
import de.adesso.budgeteer.core.budget.port.out.GetTagsInProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTagsInProjectService implements GetTagsInProjectUseCase {

    private final GetTagsInProjectPort getTagsInProjectPort;

    @Override
    public List<String> getTagsInProject(long projectId) {
        return getTagsInProjectPort.getTagsInProject(projectId);
    }
}
