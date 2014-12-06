package org.wickedsource.budgeteer;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Collections;
import java.util.Iterator;
=======
>>>>>>> 577299501471718f877f8aca277077cf47e449c9
import java.util.List;

public class ListUtil {

    public static <T> List<T> toArrayList(Iterable<T> iterable) {
        List<T> list = new ArrayList<T>();
        for (T element : iterable) {
            list.add(element);
        }
        return list;
    }
}
