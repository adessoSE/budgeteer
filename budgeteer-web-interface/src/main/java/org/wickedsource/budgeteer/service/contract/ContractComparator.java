package org.wickedsource.budgeteer.service.contract;

import java.util.Comparator;

public class ContractComparator implements Comparator<ContractBaseData> {
    @Override
    public int compare(ContractBaseData o1, ContractBaseData o2) {
        if (o1.getSortingIndex() == null && o2.getSortingIndex() == null) {
            return 0;
        } else if (o1.getSortingIndex() == null) {
            return -1;
        } else if (o2.getSortingIndex() == null) {
            return 1;
        } else {
            return o1.getSortingIndex().compareTo(o2.getSortingIndex());
        }
    }
}
