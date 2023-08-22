package org.wickedsource.budgeteer.web.components.daterange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class DateInputField extends TextField<java.util.Date> {
  protected static DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");

  public enum DROP_LOCATION {
    UP,
    DOWN
  }

  private final HashMap<String, String> options = new HashMap<>();

  public DateInputField(String id, IModel<Date> model) {
    this(id, model, DROP_LOCATION.DOWN);
  }

  /** Creates a DateRangeInputField which displays the given dateRange when opened */
  public DateInputField(String id, IModel<Date> model, DROP_LOCATION dropLocation) {
    super(id, model);
    setType(Date.class);
    options.put("format", "'DD.MM.YYYY'");
    setDates();
    options.put("singleDatePicker", "true");
    options.put("drops", dropLocation == DROP_LOCATION.DOWN ? "'down'" : "'up'");
    options.put("'linkedCalendars'", "false");
    options.put("enableEmptyDate", "true");
    super.add(new DateRangePickerBehavior(options));
  }

  @Override
  protected void onModelChanged() {
    setDates();
  }

  @Override
  protected String[] getInputTypes() {
    return new String[] {"text"};
  }

  private void setDates() {
    if (getModel().getObject() != null) {
      options.put("startDate", "'" + FORMAT.format(getModel().getObject()) + "'");
      options.put("endDate", "'" + FORMAT.format(getModel().getObject()) + "'");
    }
  }
}
