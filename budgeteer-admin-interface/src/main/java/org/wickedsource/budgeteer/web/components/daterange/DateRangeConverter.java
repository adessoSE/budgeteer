package org.wickedsource.budgeteer.web.components.daterange;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.wickedsource.budgeteer.service.DateRange;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateRangeConverter implements IConverter<DateRange> {

    private static DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public DateRange convertToObject(String value, Locale locale){
        if(value == null || (value != null && value.isEmpty())){
            return new DateRange(null, null);
        }
        try {
            Pattern pattern = Pattern.compile("^([0-9]{2}.[0-9]{2}.[0-9]{4}) - ([0-9]{2}.[0-9]{2}.[0-9]{4})$");
            Matcher matcher = pattern.matcher(value);
            if (matcher.matches()) {
                String startString = matcher.group(1);
                String endString = matcher.group(2);
                Date startDate = format.parse(startString);
                Date endDate = format.parse(endString);
                return new DateRange(startDate, endDate);
            } else {
                throw new ConversionException(String.format("Input (%s)does not match the expected date range format!", value));
            }
        } catch (ParseException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String convertToString(DateRange value, Locale locale) {
        if(value != null && value.getStartDate() != null && value.getEndDate() != null) {
            String startString = format.format(value.getStartDate());
            String endString = format.format(value.getEndDate());
            return String.format("%s - %s", startString, endString);
        }
        return "";
    }
}
