package de.adesso.budgeteer.rest.person.model;

import lombok.Value;
import org.joda.money.Money;

import java.util.List;

@Value
public class PersonWithRatesModel {
    long id;
    String name;
    String importKey;
    Money defaultDailyRate;
    List<PersonRateModel> rates;
}
