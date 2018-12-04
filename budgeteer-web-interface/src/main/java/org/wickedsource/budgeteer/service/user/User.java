package org.wickedsource.budgeteer.service.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private long id;
    private String name;
    private Date lastLogin;
}
