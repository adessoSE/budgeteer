package org.wickedsource.budgeteer.web.components.daterange;


import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DateInputField extends TextField<java.util.Date> {
    protected static DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    public static enum DROP_LOCATION {UP, DOWN}

    public DateInputField(String id, IModel<Date> model) {
        this(id, model, DROP_LOCATION.DOWN);
    }

    /**
     * Creates a DateRangeInputField which displays the given dateRange when opened
     */
    public DateInputField(String id, IModel<Date> model,  DROP_LOCATION drop_location) {
        super(id, model);
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("format","'MM/DD/YYYY'");
        if(model.getObject() != null){
            options.put("startDate", "'"+format.format(model.getObject())+"'");
            options.put("endDate", "'"+format.format(model.getObject())+"'");
        }
        options.put("singleDatePicker", "true");
        options.put("drops", drop_location == DROP_LOCATION.DOWN ? "'down'" : "'up'");
        options.put("'linkedCalendars'", "false");
        options.put("enableEmptyDate", "true");
        super.add(new DateRangePickerBehavior(options));
    }

    @Override
    protected String[] getInputTypes() {
        return new String[] { "text" };
    }

    @Override
    public <Date> IConverter<Date> getConverter(Class<Date> type) {
        return (IConverter<Date>) new DateConverter();
    }
}
