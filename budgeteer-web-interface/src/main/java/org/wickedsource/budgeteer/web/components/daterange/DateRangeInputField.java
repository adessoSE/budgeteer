package org.wickedsource.budgeteer.web.components.daterange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.DateRange;

public class DateRangeInputField extends TextField<DateRange> {

  public enum DROP_LOCATION {
    UP,
    DOWN
  }

  private final DateRange defaultDateRange;
  private final HashMap<String, String> options = new HashMap<>();
  private static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");

  public DateRangeInputField(String id, IModel<DateRange> model, DROP_LOCATION dropLocation) {
    this(id, model, null, dropLocation);
  }

  public DateRangeInputField(String id, IModel<DateRange> model) {
    this(id, model, null, DROP_LOCATION.DOWN);
  }

  /** Creates a DateRangeInputField which displays the given dateRange when opened */
  public DateRangeInputField(
      String id, IModel<DateRange> model, DateRange defaultRange, DROP_LOCATION dropLocation) {
    super(id, model);
    setType(DateRange.class);
    this.defaultDateRange = defaultRange;
    if (defaultRange != null
        && (defaultRange.getStartDate() != null || defaultRange.getEndDate() != null)) {
      options.put("format", "'dd.MM.YYYY'");
      setDates();
    }
    options.put("drops", dropLocation == DROP_LOCATION.DOWN ? "'down'" : "'up'");
    options.put("'linkedCalendars'", "false");
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
    var modelObject = getModel().getObject();
    if (defaultDateRange.getStartDate() != null) {
      options.put(
          "startDate",
          "'"
              + FORMAT.format(
                  modelObject.getStartDate() == null
                      ? defaultDateRange.getStartDate()
                      : modelObject.getStartDate())
              + "'");
    }
    if (defaultDateRange.getEndDate() != null) {
      options.put(
          "endDate",
          "'"
              + FORMAT.format(
                  modelObject.getEndDate() == null
                      ? defaultDateRange.getEndDate()
                      : modelObject.getEndDate())
              + "'");
    }
  }
}
