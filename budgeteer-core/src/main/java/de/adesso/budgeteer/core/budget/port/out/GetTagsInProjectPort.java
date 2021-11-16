package de.adesso.budgeteer.core.budget.port.out;

import java.util.List;

public interface GetTagsInProjectPort {
    List<String> getTagsInProject(long projectId);
}
