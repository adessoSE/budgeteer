package org.wickedsource.budgeteer.service.user;

import lombok.Getter;
import lombok.Setter;
import org.wickedsource.budgeteer.web.components.user.UserRole;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class User implements Serializable {
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String name;

    @Setter
    private Map<Long, List<UserRole>> roles;

    @Getter
    @Setter
    private UserRole globalRole;

    @Getter
    @Setter
    private String mail;

    public List<UserRole> getRoles(long projectId) {
        List<UserRole> userRoles;
        if (roles == null) {
            roles = new HashMap<>();
            roles.put(projectId, Collections.singletonList(UserRole.USER));
            userRoles = roles.get(projectId);
        } else {
            userRoles = roles.get(projectId);
            if (userRoles == null) {
                //ToDo?
            }
        }
        return userRoles;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(User.class)) {
            User other = (User) obj;
            return this.id == other.id && this.name.equals(other.name) &&
                    this.globalRole == other.globalRole;
        } else {
            return false;
        }
    }
}
