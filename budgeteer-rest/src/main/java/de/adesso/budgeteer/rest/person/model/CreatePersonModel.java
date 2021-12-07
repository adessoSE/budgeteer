package de.adesso.budgeteer.rest.person.model;

import de.adesso.budgeteer.core.person.domain.PersonRate;
import lombok.Data;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreatePersonModel {
    @NotEmpty
    String personName;

    @NotEmpty
    String importKey;

    Money defaultDailyRate;

    @Positive
    long projectId;

    List<PersonRate> rates = new ArrayList<>();
}
