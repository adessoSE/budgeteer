package org.wickedsource.budgeteer.service;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMapper<S, T> {

	public abstract T map(S sourceObject);

	public List<T> map(List<S> sourceObjects) {
		List<T> result = new ArrayList<T>();
		for (S source : sourceObjects) {
			result.add(map(source));
		}
		return result;
	}
}
