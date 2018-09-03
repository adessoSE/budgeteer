package org.wickedsource.budgeteer.service.person;

import java.util.Comparator;

public class RatesComparator implements Comparator<PersonRate> {
    @Override
    public int compare(PersonRate o1, PersonRate o2) {
        return o1.getBudget().getName().compareToIgnoreCase(o2.getBudget().getName());
    }
}