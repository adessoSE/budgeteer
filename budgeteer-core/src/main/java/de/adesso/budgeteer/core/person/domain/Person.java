package de.adesso.budgeteer.core.person.domain;

import lombok.*;
import org.joda.money.Money;

import java.time.LocalDate;

@Value
public class Person {
    Long id;
    String name;
    Money avarageDailyRate;
    LocalDate firstBooked;
    LocalDate lastBooked;
    Money defaultDailyRate;
    Double hoursBooked;
    Money budgetBurned;
}
