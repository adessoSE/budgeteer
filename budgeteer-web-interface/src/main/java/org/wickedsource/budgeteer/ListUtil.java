package org.wickedsource.budgeteer;

import java.util.ArrayList;
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
