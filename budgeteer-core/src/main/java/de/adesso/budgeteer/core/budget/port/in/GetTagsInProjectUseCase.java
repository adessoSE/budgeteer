package de.adesso.budgeteer.core.budget.port.in;

import java.util.List;

public interface GetTagsInProjectUseCase {
    List<String> getTagsInProject(long projectId);
}
