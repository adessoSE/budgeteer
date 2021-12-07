package de.adesso.budgeteer.core.person.port.in;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.domain.PersonRate;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;

public interface UpdatePersonUseCase {
    Person updatePerson(UpdatePersonCommand command);

    @Value
    class UpdatePersonCommand {
        long personId;
        String personName;
        String importKey;
        Money defaultDailyRate;
        List<PersonRate> rates;
    }
}
