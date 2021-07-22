package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.domain.User;

import java.util.List;

public interface GetUsersInProjectUseCase {
    List<User> getUsersInProject(long projectId);
}
