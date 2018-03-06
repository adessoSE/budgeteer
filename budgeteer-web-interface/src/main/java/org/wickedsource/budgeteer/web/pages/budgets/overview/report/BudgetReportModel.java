package org.wickedsource.budgeteer.web.pages.budgets.overview.report;

import java.io.File;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.report.BudgetReportData;
import org.wickedsource.budgeteer.service.report.BudgetReportMetaInformation;
import org.wickedsource.budgeteer.service.report.ReportService;

public class BudgetReportModel extends LoadableDetachableModel<File> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    @SpringBean
    private ReportService reportService;

    private long projectId;

    private IModel<BudgetTagFilter> filterModel;
    
    private IModel<BudgetReportMetaInformation> reportModel;

    public BudgetReportModel(long projectId, IModel<BudgetTagFilter> filterModel, IModel<BudgetReportMetaInformation> reportModel) {
        Injector.get().inject(this);
        this.filterModel = filterModel;
        this.projectId = projectId;
        this.reportModel = reportModel;
    }

    @Override
    protected File load() {
    	return reportService.createReportFile(projectId,filterModel.getObject(),reportModel.getObject()); 
    }

    public void setFilter(IModel<BudgetTagFilter> filterModel) {
        this.filterModel = filterModel;
    }

}
