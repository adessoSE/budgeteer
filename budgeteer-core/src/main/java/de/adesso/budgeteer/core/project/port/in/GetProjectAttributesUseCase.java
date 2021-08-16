package de.adesso.budgeteer.core.project.port.in;

import java.util.List;

public interface GetProjectAttributesUseCase {
    List<String> getProjectAttributes(long projectId);
}
