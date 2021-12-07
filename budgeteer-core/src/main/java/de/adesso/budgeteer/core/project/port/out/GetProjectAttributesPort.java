package de.adesso.budgeteer.core.project.port.out;

import java.util.List;

public interface GetProjectAttributesPort {
    List<String> getProjectAttributes(long projectId);
}
