package org.wickedsource.budgeteer.web.pages.contract.overview;

import java.io.File;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.contract.report.ContractReportService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.contract.edit.EditContractPage;
import org.wickedsource.budgeteer.web.pages.contract.overview.table.ContractOverviewTable;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;


@Mount("contracts")
public class ContractOverviewPage extends BasePage{
	
	@SpringBean
	private ContractReportService reportService;

    public ContractOverviewPage() {
        ContractOverviewTable table = new ContractOverviewTable("contractTable");
        add(table);
        add(new Link("createContractLink") {
            @Override
            public void onClick() {
                WebPage page = new EditContractPage(ContractOverviewPage.class, getPageParameters());
                setResponsePage(page);
            }
        });
        
        IModel<File> fileModel = new AbstractReadOnlyModel<File>() {

			@Override
			public File getObject() {
				return reportService.createReportFile(BudgeteerSession.get().getProjectId());
			}
		};
        
        add(new DownloadLink("createReportLink", fileModel,"contract-report.xlsx"));

    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, ContractOverviewPage.class);
    }
}
