package de.adesso.budgeteer.rest.person.model;

import de.adesso.budgeteer.core.person.domain.PersonRate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.joda.money.Money;

@Data
public class CreatePersonModel {
  @NotEmpty String personName;

  @NotEmpty String importKey;

  Money defaultDailyRate;

  List<PersonRate> rates = new ArrayList<>();
}
