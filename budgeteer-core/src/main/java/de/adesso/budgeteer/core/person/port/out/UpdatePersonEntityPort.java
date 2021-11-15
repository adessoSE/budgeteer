package de.adesso.budgeteer.core.person.port.out;

import de.adesso.budgeteer.core.person.domain.PersonRate;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;

public interface UpdatePersonEntityPort {
    void updatePerson(UpdatePersonEntityCommand command);

    @Value
    class UpdatePersonEntityCommand {
        long personId;
        String personName;
        String importKey;
        Money defaultDailyRate;
        List<PersonRate> rates;
    }
}
