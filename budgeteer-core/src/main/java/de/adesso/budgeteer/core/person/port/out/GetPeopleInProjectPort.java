package de.adesso.budgeteer.core.person.port.out;

import de.adesso.budgeteer.core.person.domain.Person;

import java.util.List;

public interface GetPeopleInProjectPort {
    List<Person> getPeopleInProject(Long projectId);
}
