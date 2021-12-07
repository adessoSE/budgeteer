package de.adesso.budgeteer.core.person.port.out;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.domain.PersonRate;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;

public interface CreatePersonEntityPort {
    Person createPersonEntity(CreatePersonEntityCommand command);

    @Value
    class CreatePersonEntityCommand {
        String personName;
        String importKey;
        Money defaultDailyRate;
        long projectId;
        List<PersonRate> rates;
    }
}
