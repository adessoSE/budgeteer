package org.wickedsource.budgeteer.web.components.nullmodel;

import lombok.Data;
import org.apache.wicket.model.IModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class NullsafeDateModel implements IModel<String> {
    private Date date;
    private String nullString;
    private SimpleDateFormat dateFormat;

    public NullsafeDateModel(Date date, String nullString, SimpleDateFormat format) {
        this.date = date;
        this.nullString = nullString;
        this.dateFormat = removeTimeFromDateFormat(format);
    }

    private SimpleDateFormat removeTimeFromDateFormat(SimpleDateFormat format) {
        String pattern = format.toPattern();
        String[] patternParts = pattern.split(" ");

        StringBuilder stringBuilder = new StringBuilder();

        for (String part : patternParts) {
            if (!isTime(part)) {
                stringBuilder.append(part);
            }
        }

        return new SimpleDateFormat(stringBuilder.toString());
    }

    private boolean isTime(String pattern) {
        return pattern.contains("H") || pattern.contains("m") || pattern.contains("s");
    }

    @Override
    public String getObject() {
        if (date == null) {
            return nullString;
        } else {
            return dateFormat.format(date);
        }
    }

    @Override
    public void setObject(String object) {
        try {
            date = dateFormat.parse(object);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void detach() {
        date = null;
    }
}
