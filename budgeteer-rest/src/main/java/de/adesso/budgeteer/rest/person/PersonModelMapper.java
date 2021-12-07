package de.adesso.budgeteer.rest.person;

import de.adesso.budgeteer.common.date.DateRange;
import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.domain.PersonRate;
import de.adesso.budgeteer.core.person.domain.PersonWithRates;
import de.adesso.budgeteer.rest.person.model.PersonModel;
import de.adesso.budgeteer.rest.person.model.PersonRateModel;
import de.adesso.budgeteer.rest.person.model.PersonWithRatesModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonModelMapper {
    public PersonModel toPersonModel(Person person) {
        return new PersonModel(
                person.getId(),
                person.getName(),
                person.getAvarageDailyRate(),
                person.getFirstBooked(),
                person.getLastBooked(),
                person.getDefaultDailyRate(),
                person.getHoursBooked(),
                person.getBudgetBurned()
        );
    }

    public List<PersonModel> toPersonModel(List<Person> personList) {
        return personList.stream()
                .map(this::toPersonModel)
                .collect(Collectors.toList());
    }

    public PersonWithRatesModel toPersonWithRatesModel(PersonWithRates personWithRates) {
        return new PersonWithRatesModel(
                personWithRates.getPersonId(),
                personWithRates.getName(),
                personWithRates.getImportKey(),
                personWithRates.getDefaultDailyRate(),
                toPersonRateModel(personWithRates.getRates())
        );
    }

    private PersonRateModel toPersonRateModel(PersonRate personRate) {
        return new PersonRateModel(personRate.getRate(), personRate.getBudgetId(), personRate.getDateRange());
    }

    private List<PersonRateModel> toPersonRateModel(List<PersonRate> personRates) {
        return personRates.stream()
                .map(this::toPersonRateModel)
                .collect(Collectors.toList());
    }
}
