package de.adesso.budgeteer.rest.person.model;

import lombok.Value;
import org.joda.money.Money;

import java.time.LocalDate;

@Value
public class PersonModel {
    Long id;
    String name;
    Money avarageDailyRate;
    LocalDate firstBooked;
    LocalDate lastBooked;
    Money defaultDailyRate;
    Double hoursBooked;
    Money budgetBurned;
}
