package org.wickedsource.budgeteer.web.components.burntable;

import org.apache.wicket.markup.html.panel.Panel;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.components.burntable.filter.FilterPanel;
import org.wickedsource.budgeteer.web.components.burntable.filter.FilteredRecordsModel;
import org.wickedsource.budgeteer.web.components.burntable.table.BurnTable;

public class BurnTableWithFilter extends Panel {

    private FilterPanel filterPanel;

    public BurnTableWithFilter(String id, WorkRecordFilter initialFilter) {
        this(id, initialFilter, false);
    }

    public BurnTableWithFilter(String id, WorkRecordFilter initialFilter, boolean dailyRateIsEditable) {
        super(id);

        filterPanel = new FilterPanel("filter", initialFilter);
        add(filterPanel);

        FilteredRecordsModel tableModel = new FilteredRecordsModel(initialFilter);
        BurnTable table = new BurnTable("table", tableModel, dailyRateIsEditable);
        add(table);
    }

    public boolean isPersonFilterEnabled() {
        return filterPanel.isPersonFilterEnabled();
    }

    public void setPersonFilterEnabled(boolean personFilterEnabled) {
        filterPanel.setPersonFilterEnabled(personFilterEnabled);
    }

    public boolean isBudgetFilterEnabled() {
        return filterPanel.isBudgetFilterEnabled();
    }

    public void setBudgetFilterEnabled(boolean budgetFilterEnabled) {
        filterPanel.setBudgetFilterEnabled(budgetFilterEnabled);
    }

    public boolean isDaterangeFilterEnabled() {
        return filterPanel.isDaterangeFilterEnabled();
    }

    public void setDaterangeFilterEnabled(boolean daterangeFilterEnabled) {
        filterPanel.setDaterangeFilterEnabled(daterangeFilterEnabled);
    }

}
