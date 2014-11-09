package org.wickedsource.budgeteer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListUtil {

    public static <T> List<T> toArrayList(Iterable<T> iterable) {
        List<T> list = new ArrayList<T>();
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            list.add(element);
        }
        return list;
    }
}
