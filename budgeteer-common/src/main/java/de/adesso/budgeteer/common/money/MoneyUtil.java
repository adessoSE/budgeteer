package de.adesso.budgeteer.common.money;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.Collection;
import java.util.function.Function;

public class MoneyUtil {

    private MoneyUtil() {
    }

    public static <T> Money total(Collection<T> data, Function<T, Money> mapper, CurrencyUnit currencyUnit) {
        return Money.total(currencyUnit, data.stream().map(mapper)::iterator);
    }
}
