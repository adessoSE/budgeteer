package org.wickedsource.budgeteer.web.components.daterange;


import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.wickedsource.budgeteer.service.DateRange;
import org.wicketstuff.lazymodel.LazyModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class DateRangeInputField extends TextField<DateRange> {

    public static enum DROP_LOCATION {UP, DOWN}

    public DateRangeInputField(String id, IModel<DateRange> model, DROP_LOCATION drop_location) {
        this(id, model, null, drop_location);
    }

    public DateRangeInputField(String id, IModel<DateRange> model) {
        this(id, model, null, DROP_LOCATION.DOWN);
    }

    /**
     * Creates a DateRangeInputField which displays the given dateRange when opened
     */
    public DateRangeInputField(String id, IModel<DateRange> model, DateRange defaultRange, DROP_LOCATION drop_location) {
        super(id, model);
        HashMap<String, String> options = new HashMap<String, String>();
        if(defaultRange != null && (defaultRange.getStartDate() != null || defaultRange.getEndDate() != null)){
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            options.put("format","'dd.MM.YYYY'");
            DateRange modelObject = model.getObject();
            if(defaultRange.getStartDate() != null){
                options.put("startDate", "'"+format.format(modelObject.getStartDate() == null ? defaultRange.getStartDate() : modelObject.getStartDate())+"'");
            }
            if(defaultRange.getEndDate() != null){
                options.put("endDate", "'"+format.format(modelObject.getEndDate() == null ? defaultRange.getEndDate() : modelObject.getEndDate())+"'");
            }
        }
        options.put("drops", drop_location == DROP_LOCATION.DOWN ? "'down'" : "'up'");
        options.put("'linkedCalendars'", "false");
        super.add(new DateRangePickerBehavior(options));
    }


    @Override
    @SuppressWarnings("unchecked")
    public <DateRange> IConverter<DateRange> getConverter(Class<DateRange> type) {
        return (IConverter<DateRange>) new DateRangeConverter();
    }

    @Override
    protected String[] getInputTypes() {
        return new String[]{"text"};
    }


}
