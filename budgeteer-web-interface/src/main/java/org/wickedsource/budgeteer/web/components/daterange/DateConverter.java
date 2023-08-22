package org.wickedsource.budgeteer.web.components.daterange;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

public class DateConverter implements IConverter<Date> {
  @Override
  public Date convertToObject(String value, Locale locale) {
    if (value.isEmpty()) return null;
    try {
      return DateInputField.FORMAT.parse(value);
    } catch (ParseException e) {
      throw new ConversionException(e);
    }
  }

  @Override
  public String convertToString(Date value, Locale locale) {
    if (value == null) {
      return "";
    }
    return DateInputField.FORMAT.format(value);
  }
}
