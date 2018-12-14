package de.adesso.budgeteer.web.pages.budgets.overview.report;

import de.adesso.budgeteer.service.budget.BudgetTagFilter;
import de.adesso.budgeteer.service.budget.report.BudgetReportService;
import de.adesso.budgeteer.service.budget.report.ReportMetaInformation;
import de.adesso.budgeteer.service.template.Template;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;

public class BudgetReportFileModel extends LoadableDetachableModel<File> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    @SpringBean
    private BudgetReportService reportService;

    private long projectId;

    private IModel<BudgetTagFilter> filterModel;
    
    private IModel<ReportMetaInformation> reportModel;

    private IModel<Template> templateIModel;

    public BudgetReportFileModel(long projectId, IModel<BudgetTagFilter> filterModel, IModel<ReportMetaInformation> reportModel, IModel<Template> templateIModel) {
        Injector.get().inject(this);
        this.filterModel = filterModel;
        this.projectId = projectId;
        this.reportModel = reportModel;
        this.templateIModel = templateIModel;
    }

    @Override
    protected File load() {
        return reportService.createReportFile(templateIModel.getObject().getId(), projectId,filterModel.getObject(),reportModel.getObject());
    }

    public void setFilter(IModel<BudgetTagFilter> filterModel) {
        this.filterModel = filterModel;
    }

}
