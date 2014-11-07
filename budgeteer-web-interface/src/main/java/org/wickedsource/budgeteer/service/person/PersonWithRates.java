package org.wickedsource.budgeteer.service.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonWithRates implements Serializable {

    private long personId;

    private String name;

    private String importKey;

    private List<PersonRate> rates = new ArrayList<PersonRate>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public List<PersonRate> getRates() {
        return rates;
    }

    public void setRates(List<PersonRate> rates) {
        this.rates = rates;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }
}
