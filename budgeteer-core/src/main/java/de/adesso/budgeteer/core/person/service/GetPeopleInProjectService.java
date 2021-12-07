package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.port.in.GetPeopleInProjectUseCase;
import de.adesso.budgeteer.core.person.port.out.GetPeopleInProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPeopleInProjectService implements GetPeopleInProjectUseCase {
    private final GetPeopleInProjectPort getPeopleInProjectPort;

    @Override
    public List<Person> getPeopleInProject(Long projectId) {
        return getPeopleInProjectPort.getPeopleInProject(projectId);
    }
}
