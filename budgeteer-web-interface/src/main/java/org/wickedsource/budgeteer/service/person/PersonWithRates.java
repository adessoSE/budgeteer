package org.wickedsource.budgeteer.service.person;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PersonWithRates implements Serializable {

    private long personId;
    private String name;
    private String importKey;
    private List<PersonRate> rates = new ArrayList<PersonRate>();
}
