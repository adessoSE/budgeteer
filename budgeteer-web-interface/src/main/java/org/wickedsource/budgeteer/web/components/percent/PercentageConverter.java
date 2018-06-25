package org.wickedsource.budgeteer.web.components.percent;

import java.util.Locale;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

public class PercentageConverter implements IConverter<Double> {

	@Override
	public Double convertToObject(String value, Locale locale) throws ConversionException {
		throw new NotImplementedException("This converter is a read-only converter and only supports converting one-way");
	}

	@Override
	public String convertToString(Double value, Locale locale) {
		return String.format("%.2f%%", value * 100);
	}

}
