package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.port.in.GetPeopleInBudgetUseCase;
import de.adesso.budgeteer.core.person.port.out.GetPeopleInBudgetPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPeopleInBudgetService implements GetPeopleInBudgetUseCase {
    private final GetPeopleInBudgetPort getPeopleInBudgetPort;

    @Override
    public List<Person> getPeopleInBudget(long budgetId) {
        return getPeopleInBudgetPort.getPeopleInBudget(budgetId);
    }
}
