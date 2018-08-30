package de.adesso.budgeteer.web.components.money;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MoneyConverter implements IConverter<Money> {

    private boolean prependCurrencySymbol;

    private boolean format = true;

    public MoneyConverter() {
    }

    public boolean isPrependCurrencySymbol() {
        return prependCurrencySymbol;
    }

    public void setPrependCurrencySymbol(boolean prependCurrencySymbol) {
        this.prependCurrencySymbol = prependCurrencySymbol;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public MoneyConverter(boolean prependCurrencySymbol) {
        this.prependCurrencySymbol = prependCurrencySymbol;
    }

    /**
     * @param value     The given value, to be converted.
     * @param locale    Locale of a certain country. Depending on the country the currency is chosen.
     * If format is set to false, the converter tries to parse the given value to a double.
     * If format is true, the method convertToObject converts Strings of certain patterns.
     * The pattern has to be;
     *                          123.456,78  = 123456,78 EUR
     *                          12.34.56    = 123456 EUR
     *                          123456,78   = 123456,78 EUR
     *                          123         = 123 EUR           and so on..
     *
     * The pattern does not allow;
     *                          abc = no alphabetic characters
     *                          123456,789  = 123456,78 EUR     only two digits behind the comma are allowed.
     * */
    @Override
    public Money convertToObject(String value, Locale locale) throws ConversionException {
        try {
            Number number;
            if (isFormat()) {
                NumberFormat format = NumberFormat.getInstance(locale);
                number = format.parse(value);
            } else {
                number = Double.valueOf(value);
            }
            return MoneyUtil.createMoney(number.doubleValue());
        } catch (ParseException | ArithmeticException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String convertToString(Money value, Locale locale) {
        String formatted;
        if (isFormat()) {
            NumberFormat format = NumberFormat.getInstance(locale);
            format.setMinimumFractionDigits(2);
            formatted = format.format(value.getAmount().doubleValue());
        } else {
            formatted = String.valueOf(value.getAmount().doubleValue());
        }
        if (prependCurrencySymbol) {
            formatted = value.getCurrencyUnit().getSymbol() + " " + formatted;
        }
        return formatted;
    }
}
