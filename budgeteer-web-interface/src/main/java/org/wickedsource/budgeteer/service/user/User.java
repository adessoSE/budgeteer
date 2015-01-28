package org.wickedsource.budgeteer.service.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private long id;
    private String name;
}
