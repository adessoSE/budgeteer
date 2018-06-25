package org.wickedsource.budgeteer;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

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
		List<Double> doubleValues = new ArrayList<Double>();
		for (Money moneyValue : moneyList) {
			doubleValues.add(moneyValue.getAmount().doubleValue());
		}
		return doubleValues;
	}

	public static List<Double> toDouble(List<Money> moneyList, Double unit) {
		List<Double> doubleValues = new ArrayList<Double>();
		for (Money moneyValue : moneyList) {
			doubleValues.add(toDouble(moneyValue, unit));
		}
		return doubleValues;
	}

	public static List<Double> toDouble(List<Money> moneyList, Double unit, Double taxrate) {
		List<Double> doubleValues = new ArrayList<Double>();
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
}
