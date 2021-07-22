package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.domain.User;

import java.util.List;

public interface GetUsersNotInProjectUseCase {
    List<User> getUsersNotInProject(long projectId);
}
