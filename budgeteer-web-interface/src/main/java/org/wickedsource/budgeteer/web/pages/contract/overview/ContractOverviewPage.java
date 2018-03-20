package org.wickedsource.budgeteer.web.pages.contract.overview;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.contract.edit.EditContractPage;
import org.wickedsource.budgeteer.web.pages.contract.overview.report.ContractReportDialog;
import org.wickedsource.budgeteer.web.pages.contract.overview.table.ContractOverviewTable;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;


@Mount("contracts")
public class ContractOverviewPage extends BasePage{

    @SpringBean
    private ContractService contractService;

    private ContractOverviewTable table;

    private Fragment frag;

    public ContractOverviewPage() {
        table = new ContractOverviewTable("contractTable");

        add(table);
        add(new Link("createContractLink") {
            @Override
            public void onClick() {
                WebPage page = new EditContractPage(ContractOverviewPage.class, getPageParameters());
                setResponsePage(page);
            }
        });


        add(createReportLink("createReportLink"));
        createNetGrossSwitchLink("netGrossLink");
    }

    private void createNetGrossSwitchLink(String netGrossLink) {
        if (BudgeteerSession.get().isTaxEnabled()) {
            frag = new Fragment("netGrossLinkContainer", "netLinkFragment", this);
            frag.add(getSwitchLink(netGrossLink));
        } else {
            frag = new Fragment("netGrossLinkContainer", "grossLinkFragment", this);
            frag.add(getSwitchLink(netGrossLink));
        }
        frag.setOutputMarkupId(true);
        add(frag);
    }

    private Component getSwitchLink(String string) {
        return new Link(string) {
            @Override
            public void onClick() {
                if (BudgeteerSession.get().isTaxEnabled()) {
                    BudgeteerSession.get().setTaxEnabled(false);
                } else {
                    BudgeteerSession.get().setTaxEnabled(true);
                }
                setResponsePage(ContractOverviewPage.class);
            }
        };
    }


    private Component createReportLink(String string) {
        Link link = new Link(string) {
            @Override
            public void onClick() {
                setResponsePage(new ContractReportDialog(ContractOverviewPage.class, new PageParameters()));
            }
        };

        if (!contractService.projectHasContracts(BudgeteerSession.get().getProjectId())) {
            link.setEnabled(false);
            link.add(new AttributeAppender("style", "cursor: not-allowed;", " "));
            link.add(new AttributeModifier("title", ContractOverviewPage.this.getString("links.contract.label.no.contract")));
        }
        return link;
	}

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, ContractOverviewPage.class);
    }
}
