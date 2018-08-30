package org.wickedsource.budgeteer.service.user;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

@Data
public class User implements Serializable {
    private long id;
    private String name;
    private Map<Long, ArrayList<String>> roles;
    private String globalRole;
}
