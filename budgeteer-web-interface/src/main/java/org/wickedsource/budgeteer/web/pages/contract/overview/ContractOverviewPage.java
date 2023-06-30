package org.wickedsource.budgeteer.web.pages.contract.overview;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.StringResourceModel;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.tax.TaxSwitchLabelModel;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.contract.edit.EditContractPage;
import org.wickedsource.budgeteer.web.pages.contract.overview.table.ContractOverviewTable;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("contracts")
public class ContractOverviewPage extends BasePage {

  private ContractOverviewTable table;

  private Fragment frag;

  public ContractOverviewPage() {
    table = new ContractOverviewTable("contractTable");

    add(table);
    add(
        new Link("createContractLink") {
          @Override
          public void onClick() {
            WebPage page = new EditContractPage(ContractOverviewPage.class, getPageParameters());
            setResponsePage(page);
          }
        });

    add(createNetGrossLink("netGrossLink"));
  }

  private Link createNetGrossLink(String string) {
    Link link =
        new Link(string) {
          @Override
          public void onClick() {
            if (BudgeteerSession.get().isTaxEnabled()) {
              BudgeteerSession.get().setTaxEnabled(false);
            } else {
              BudgeteerSession.get().setTaxEnabled(true);
            }
          }
        };
    link.add(
        new Label(
            "title",
            new TaxSwitchLabelModel(
                new StringResourceModel("links.tax.label.net", this),
                new StringResourceModel("links.tax.label.gross", this))));
    return link;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected BreadcrumbsModel getBreadcrumbsModel() {
    return new BreadcrumbsModel(DashboardPage.class, ContractOverviewPage.class);
  }
}
