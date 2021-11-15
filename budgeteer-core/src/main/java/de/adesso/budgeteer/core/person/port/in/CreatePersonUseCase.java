package de.adesso.budgeteer.core.person.port.in;

import de.adesso.budgeteer.core.person.domain.PersonRate;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;

public interface CreatePersonUseCase {
    void createPerson(CreatePersonCommand command);

    @Value
    class CreatePersonCommand {
        long personId;
        String personName;
        String importKey;
        Money defaultDailyRate;
        long projectId;
        List<PersonRate> rates;
    }
}
