package org.wickedsource.budgeteer.web.component.burntable;

import org.apache.wicket.markup.html.panel.Panel;
import org.wickedsource.budgeteer.service.record.RecordFilter;
import org.wickedsource.budgeteer.web.component.burntable.filter.FilterPanel;
import org.wickedsource.budgeteer.web.component.burntable.filter.FilteredRecordsModel;
import org.wickedsource.budgeteer.web.component.burntable.table.BurnTable;

public class BurnTableWithFilter extends Panel {

    private FilterPanel filterPanel;

    public BurnTableWithFilter(String id, RecordFilter initialFilter) {
        super(id);

        filterPanel = new FilterPanel("filter", initialFilter);
        add(filterPanel);

        FilteredRecordsModel tableModel = new FilteredRecordsModel(initialFilter);
        BurnTable table = new BurnTable("table", tableModel);
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
