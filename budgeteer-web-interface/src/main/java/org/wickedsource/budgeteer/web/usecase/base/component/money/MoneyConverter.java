package org.wickedsource.budgeteer.web.usecase.base.component.money;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.MoneyUtil;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MoneyConverter implements IConverter<Money> {

    @Override
    public Money convertToObject(String value, Locale locale) throws ConversionException {
        try {
            NumberFormat format = NumberFormat.getInstance(locale);
            Number number = format.parse(value);
            return MoneyUtil.createMoney(number.doubleValue());
        } catch (ParseException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String convertToString(Money value, Locale locale) {
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.format(value.getAmount().doubleValue());
    }
}
