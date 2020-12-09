package org.wickedsource.budgeteer.service.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonWithRates implements Serializable {

    private long personId;
    private String name;
    private String importKey;
    private Money defaultDailyRate;
    private List<PersonRate> rates = new ArrayList<>();
}
