package org.wickedsource.budgeteer.service.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.wickedsource.budgeteer.web.components.user.UserRole;

import java.io.Serializable;
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

    public List<UserRole> getRoles(long projectId){
        if(roles == null){
            roles = new HashMap<>();
            roles.put(projectId, Collections.singletonList(UserRole.USER));
            return roles.get(projectId);
        }else{
            return roles.get(projectId);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(User.class)) {
            User other = (User) obj;
            return this.id == other.id && this.name.equals(other.name) &&
                    this.globalRole == other.globalRole;
        }else{
            return false;
        }
    }
}
