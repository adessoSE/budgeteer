package org.wickedsource.budgeteer;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MoneyUtil {

    public static CurrencyUnit DEFAULT_CURRENCY = CurrencyUnit.EUR;

    public static Money ZERO = Money.of(DEFAULT_CURRENCY, 0d);

    public static Money createMoney(double value) {
        return Money.of(DEFAULT_CURRENCY, value, RoundingMode.HALF_UP);
    }

    public static Money createMoneyFromCents(long cents) {
        return Money.ofMinor(DEFAULT_CURRENCY, cents);
    }

    public static List<Double> toDouble(List<Money> moneyList) {
        List<Double> doubleValues = new ArrayList<>();
        for (Money moneyValue : moneyList) {
            doubleValues.add(moneyValue.getAmount().doubleValue());
        }
        return doubleValues;
    }

    public static List<Double> toDouble(List<Money> moneyList, Double unit) {
        List<Double> doubleValues = new ArrayList<>();
        for (Money moneyValue : moneyList) {
            doubleValues.add(toDouble(moneyValue, unit));
        }
        return doubleValues;
    }

    public static List<Double> toDouble(List<Money> moneyList, Double unit, Double taxrate) {
        List<Double> doubleValues = new ArrayList<>();
        for (Money moneyValue : moneyList) {
            doubleValues.add(toDouble(moneyValue, unit, taxrate));
        }
        return doubleValues;
    }

    public static Double toDouble(Money money) {
        return money.getAmount().doubleValue();
    }

    private MoneyUtil() {

    }

    public static Double toDouble(Money money, Double unit) {
        return money.dividedBy(unit, RoundingMode.FLOOR).getAmount().doubleValue();
    }

    public static Double toDouble(Money money, Double unit, Double taxRate) {
        return money.dividedBy(unit, RoundingMode.FLOOR).multipliedBy(taxRate, RoundingMode.FLOOR).getAmount().doubleValue();
    }

    /**
     * Add taxes to an amount of money.
     *
     * @param money        the amount of money
     * @param taxInPercent tax rate in percent, which should be added
     * @return the given amount with taxes added
     */
    public static Money getMoneyWithTaxes(Money money, BigDecimal taxInPercent) {
        Money taxes = MoneyUtil.getTaxAmount(money, taxInPercent);

        return money.plus(taxes);
    }

    public static Money getTaxAmount(Money money, BigDecimal taxInPercent) {
        Money taxes = money.multipliedBy(taxInPercent, RoundingMode.HALF_UP);
        return taxes.dividedBy(100, RoundingMode.HALF_UP);
    }

    /**
     * Sum up two amounts of money, even if one value is null.
     *
     * @param firstValue  first money amount
     * @param secondValue second money amount
     * @return sum of the two amounts
     */
    public static Money sumMoney(Money firstValue, Money secondValue) {
        Money money;
        if (firstValue == null) {
            money = secondValue;
        } else if (secondValue == null) {
            money = firstValue;
        } else {
            money = firstValue.plus(secondValue);
        }

        return money;
    }

    public static long getCentsByHourFraction(long monthValueInCents, Double monthHours, Double weekHours) {
        if (monthHours == 0 || weekHours == 0) {
            return 0;
        } else {
            return Math.round(monthValueInCents * weekHours / monthHours);
        }
    }

    public static long getCentsWithTaxes(long valueInCents, BigDecimal taxRate) {
        return Math.round(valueInCents * (1 + taxRate.doubleValue() / 100));
    }

    public static Money getMoneyNullsafe(Money money) {
        if (money == null) {
            return MoneyUtil.createMoneyFromCents(0L);
        } else {
            return money;
        }
    }

    public static Money toMoneyNullsafe(Double cents) {
        if (cents == null) {
            return MoneyUtil.createMoneyFromCents(0L);
        } else {
            return MoneyUtil.createMoneyFromCents(Math.round(cents));
        }
    }
}
