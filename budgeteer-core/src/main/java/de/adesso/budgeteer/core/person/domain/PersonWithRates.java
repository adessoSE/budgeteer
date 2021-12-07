package de.adesso.budgeteer.core.person.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.joda.money.Money;

import java.time.LocalDate;
import java.util.List;

@Value
public class PersonWithRates {
    long personId;
    String name;
    String importKey;
    Money defaultDailyRate;
    List<PersonRate> rates;
}
