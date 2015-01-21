package org.wickedsource.budgeteer.web.components.money;

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
        } catch (ParseException e ) {
            throw new ConversionException(e);
        } catch (ArithmeticException e){
            throw new ConversionException(e);
        }
    }

    @Override
    public String convertToString(Money value, Locale locale) {
        String formatted;
        if (isFormat()) {
            NumberFormat format = NumberFormat.getInstance(locale);
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
