package de.adesso.budgeteer.core.user.port.out;

import de.adesso.budgeteer.core.user.domain.User;

import java.util.List;

public interface GetUsersInProjectPort {
    List<User> getUsersInProject(long projectId);
}
