package org.wickedsource.budgeteer;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MoneyUtil {

    public static CurrencyUnit DEFAULT_CURRENCY = CurrencyUnit.EUR;

    public static Money ZERO = Money.of(DEFAULT_CURRENCY, 0d);

    public static Money createMoney(double value) {
        return Money.of(DEFAULT_CURRENCY, value);
    }

    public static Money createMoneyFromCents(long cents) {
        return Money.ofMinor(DEFAULT_CURRENCY, cents);
    }

    public static List<Double> toDouble(List<Money> moneyList) {
        List<Double> doubleValues = new ArrayList<Double>();
        for (Money moneyValue : moneyList) {
            doubleValues.add(moneyValue.getAmount().doubleValue());
        }
        return doubleValues;
    }

    public static List<Double> toDouble(List<Money> moneyList, Double unit) {
        List<Double> doubleValues = new ArrayList<Double>();
        for (Money moneyValue : moneyList) {
            doubleValues.add(moneyValue.dividedBy(unit, RoundingMode.FLOOR).getAmount().doubleValue());
        }
        return doubleValues;
    }

    public static Double toDouble(Money money) {
        return money.getAmount().doubleValue();
    }

    private MoneyUtil() {

    }

}
