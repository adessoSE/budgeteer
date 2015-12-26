package org.wickedsource.budgeteer.web.components.daterange;


import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.wickedsource.budgeteer.service.DateRange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class DateRangeInputField extends TextField<DateRange> {

    public DateRangeInputField(String id) {
        this(id, null);
    }

    public DateRangeInputField(String id, IModel<DateRange> model) {
        this(id, model, null);
    }

    /**
     * Creates a DateRangeInputField which displays the given dateRange when opened
     */
    public DateRangeInputField(String id, IModel<DateRange> model, DateRange defaultRange) {
        super(id, model);
        HashMap<String, String> options = new HashMap<String, String>();
        if(defaultRange != null && (defaultRange.getStartDate() != null || defaultRange.getEndDate() != null)){
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            options.put("format","'M/D/YYYY'");
            if(defaultRange.getStartDate() != null){
                options.put("startDate", "'"+format.format(defaultRange.getStartDate())+"'");
            }
            if(defaultRange.getEndDate() != null){
                options.put("endDate", "'"+format.format(defaultRange.getEndDate())+"'");
            }
        }
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
