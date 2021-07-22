package de.adesso.budgeteer.core.user.port.out;

import de.adesso.budgeteer.core.user.domain.User;

import java.util.List;

public interface GetUsersNotInProjectPort {
    List<User> getUsersNotInProject(long projectId);
}
