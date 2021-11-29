package org.wickedsource.budgeteer.persistence.person;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.domain.PersonWithRates;
import lombok.RequiredArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.record.RecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonMapper {
    private final PersonRateMapper personRateMapper;

    public Person mapToPerson(PersonEntity personEntity) {
        return new Person(personEntity.getId(), personEntity.getName(), calculateAvarageDailyRate(personEntity.getWorkRecords()),
                findFirstBooked(personEntity.getWorkRecords()), findLastBooked(personEntity.getWorkRecords()),
                personEntity.getDefaultDailyRate(), calculateHoursBooked(personEntity.getWorkRecords()),
                calculateBudgetBurned(personEntity.getWorkRecords()));
    }

    public List<Person> mapToPerson(List<PersonEntity> personEntities) {
        return personEntities.stream()
                .map(this::mapToPerson)
                .collect(Collectors.toList());
    }

    public PersonWithRates mapToPersonWithRates(PersonEntity personEntity) {
        return new PersonWithRates(personEntity.getId(), personEntity.getName(), personEntity.getImportKey(),
                personEntity.getDefaultDailyRate(), personRateMapper.mapToDomain(personEntity.getDailyRates()));
    }

    private Money calculateAvarageDailyRate(List<WorkRecordEntity> workRecords) {
       int totalMinutes = workRecords.stream().mapToInt(RecordEntity::getMinutes).sum();

       if(totalMinutes == 0) {
           return Money.zero(CurrencyUnit.EUR);
       }

       return workRecords.stream()
               .map(workRecordEntity -> workRecordEntity.getDailyRate().multipliedBy(workRecordEntity.getMinutes()))
               .reduce(Money.zero(CurrencyUnit.EUR), Money::plus)
               .dividedBy(totalMinutes, RoundingMode.HALF_DOWN);
    }

    private LocalDate findFirstBooked(List<WorkRecordEntity> workRecords) {
        return workRecords.stream()
                .map(RecordEntity::getDate)
                .min(Date::compareTo)
                .map(date -> new java.sql.Date(date.getTime()).toLocalDate())
                .orElse(null);
    }

    private LocalDate findLastBooked(List<WorkRecordEntity> workRecords) {
        return workRecords.stream()
                .map(RecordEntity::getDate)
                .max(Date::compareTo)
                .map(date -> new java.sql.Date(date.getTime()).toLocalDate())
                .orElse(null);
    }

    private Double calculateHoursBooked(List<WorkRecordEntity> workRecords) {
        return workRecords.stream()
                .mapToDouble(WorkRecordEntity::getHours)
                .sum();
    }

    private Money calculateBudgetBurned(List<WorkRecordEntity> workRecords) {
        return workRecords.stream()
                .map(workRecordEntity -> {
                    double workHourCoefficient = workRecordEntity.getHours() / 8.0;
                    return workRecordEntity.getDailyRate().multipliedBy(workHourCoefficient, RoundingMode.HALF_DOWN);
                })
                .reduce(Money.zero(CurrencyUnit.EUR), Money::plus);
    }
}
