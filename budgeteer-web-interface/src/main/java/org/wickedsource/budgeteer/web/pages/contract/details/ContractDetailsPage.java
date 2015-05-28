package org.wickedsource.budgeteer.web.pages.contract.details;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.components.confirm.ConfirmationForm;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.contract.details.ContractDetailChart.ContractDetailChart;
import org.wickedsource.budgeteer.web.pages.contract.details.ContractDetailChart.ContractDetailChartModel;
import org.wickedsource.budgeteer.web.pages.contract.details.belongingObject.ContractBelongingObjectsPanel;
import org.wickedsource.budgeteer.web.pages.contract.details.highlights.ContractHighlightsPanel;
import org.wickedsource.budgeteer.web.pages.contract.edit.EditContractPage;
import org.wickedsource.budgeteer.web.pages.contract.overview.ContractOverviewPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("contracts/details/${id}")
public class ContractDetailsPage extends BasePage {

    @SpringBean
    private ContractService contractService;

    public ContractDetailsPage(PageParameters parameters) {
        super(parameters);
        ContractDetailModel contractModel = new ContractDetailModel(getParameterId());

        add(new ContractHighlightsPanel("highlightsPanel", contractModel));
        add(new ContractDetailChart("comparisonChart", new ContractDetailChartModel(getParameterId(), 6), new BudgeteerChartTheme()));
        add(new ContractBelongingObjectsPanel("belongingObjects", contractModel));

        add(new Link("editLink") {
            @Override
            public void onClick() {
                WebPage page = new EditContractPage(createParameters(getParameterId()), ContractDetailsPage.class, getPageParameters());
                setResponsePage(page);
            }
        });

        Form deleteForm = new ConfirmationForm("deleteForm", this, "confirmation.delete") {
            @Override
            public void onSubmit() {
                contractService.deleteContract(getParameterId());
                setResponsePage(ContractOverviewPage.class);
            }
        };
        deleteForm.add(new SubmitLink("deleteLink"));
        add(deleteForm);
    }


    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, ContractOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(ContractDetailsPage.class, getPageParameters(), Model.of("Contract details")));
        return model;
    }

}
