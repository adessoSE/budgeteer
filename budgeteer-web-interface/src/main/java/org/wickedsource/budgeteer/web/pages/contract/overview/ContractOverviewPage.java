package org.wickedsource.budgeteer.web.pages.contract.overview;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.contract.edit.EditContractPage;
import org.wickedsource.budgeteer.web.pages.contract.overview.report.ContractReportDialog;
import org.wickedsource.budgeteer.web.pages.contract.overview.table.ContractOverviewTable;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;


@Mount("contracts")
public class ContractOverviewPage extends BasePage{
	

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
        
		add(createReportLink("createReportLink"));

    }
    
	private Component createReportLink(String string) {
		return new Link(string) {
			@Override
			public void onClick() {
				setResponsePage(new ContractReportDialog(ContractOverviewPage.class, new PageParameters()));
			}
		};
	}

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, ContractOverviewPage.class);
    }
}
