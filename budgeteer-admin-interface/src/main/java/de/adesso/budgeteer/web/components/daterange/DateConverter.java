package de.adesso.budgeteer.web.components.daterange;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DateConverter implements IConverter<Date> {
    @Override
    public Date convertToObject(String value, Locale locale){
        if(value.isEmpty())
            return null;
        try {
           return DateInputField.format.parse(value);
        } catch (ParseException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String convertToString(Date value, Locale locale) {
        if(value == null){
            return "";
        }
        return DateInputField.format.format(value);
    }
}
