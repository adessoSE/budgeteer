package org.wickedsource.budgeteer.web.components.burntable;

import java.io.File;

import javax.inject.Inject;

import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.exports.ExportService;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.components.burntable.filter.FilterPanel;
import org.wickedsource.budgeteer.web.components.burntable.filter.FilteredRecordsModel;
import org.wickedsource.budgeteer.web.components.burntable.table.BurnTable;

public class BurnTableWithFilter extends Panel {

	@Inject private ExportService exportService;

	private FilterPanel filterPanel;

	private BurnTable burnTable;

	public BurnTableWithFilter(String id, WorkRecordFilter initialFilter) {
		this(id, initialFilter, false);
	}

	public BurnTableWithFilter(
			String id, WorkRecordFilter initialFilter, boolean dailyRateIsEditable) {
		super(id);

		filterPanel = new FilterPanel("filter", initialFilter);
		add(filterPanel);

		FilteredRecordsModel tableModel = new FilteredRecordsModel(initialFilter);
		burnTable = new BurnTable("table", tableModel, dailyRateIsEditable);
		add(burnTable);

		IModel<File> fileModel =
				new AbstractReadOnlyModel<File>() {
					@Override
					public File getObject() {
						return exportService.generateCSVFileFromRecords(burnTable.getRows());
					}
				};

		DownloadLink downloadlink = new DownloadLink("export", fileModel);
		downloadlink.setDeleteAfterDownload(true);
		add(downloadlink);
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
