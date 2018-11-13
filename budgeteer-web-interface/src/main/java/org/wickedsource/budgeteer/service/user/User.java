package org.wickedsource.budgeteer.service.user;

import lombok.Data;
import org.wickedsource.budgeteer.web.components.user.UserRole;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

@Data
public class User implements Serializable {
    private long id;
    private String name;
    private Map<Long, ArrayList<String>> roles;
    private UserRole globalRole;
    private String mail;
}
