package de.adesso.budgeteer.core.common;

import java.util.*;

public class Causes<T> {
    private final Set<T> causes = new HashSet<>();

    public void addCause(T t) {
        causes.add(t);
    }

    public boolean hasCause() {
        return !causes.isEmpty();
    }

    public Set<T> getAll() {
        return Collections.unmodifiableSet(causes);
    }

    public boolean contains(T cause) {
        return causes.contains(cause);
    }

    public boolean containsAll(Collection<T> causes) {
        return this.causes.containsAll(causes);
    }
}
