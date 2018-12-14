package de.adesso.budgeteer.service.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.money.Money;

@Getter
@AllArgsConstructor
public class Share {
    private Money share;
    private String name;
}
