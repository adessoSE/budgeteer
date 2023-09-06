package org.wickedsource.budgeteer.web.pages.contract.overview.table;

import de.adesso.budgeteer.common.old.MoneyUtil;
import de.adesso.budgeteer.persistence.contract.ContractEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.service.contract.ContractTotalData;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.components.datelabel.DateLabel;
import org.wickedsource.budgeteer.web.components.money.BudgetUnitMoneyModel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.components.tax.TaxBudgetUnitMoneyModel;
import org.wickedsource.budgeteer.web.components.tax.TaxLabelModel;
import org.wickedsource.budgeteer.web.pages.contract.details.ContractDetailsPage;
import org.wickedsource.budgeteer.web.pages.contract.edit.EditContractPage;

public class ContractOverviewTable extends Panel {

  @SpringBean private ContractService contractService;

  public ContractOverviewTable(String id) {
    super(id);
    ContractOverviewTableModel data =
        contractService.getContractOverviewByProject(BudgeteerSession.get().getProjectId());
    WebMarkupContainer table = new WebMarkupContainer("table");

    createNetGrossLabels(table);

    table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));
    table.add(
        new ListView<String>("headerRow", Model.ofList(data.getHeadline())) {
          @Override
          protected void populateItem(ListItem<String> item) {
            item.add(new Label("headerItem", item.getModel()));
          }
        });
    table.add(
        new ListView<ContractBaseData>("contractRows", Model.ofList(data.getContracts())) {
          @Override
          protected void populateItem(ListItem<ContractBaseData> item) {
            long contractId = item.getModelObject().getContractId();
            BigDecimal taxCoefficient = BigDecimal.ONE;

            if (BudgeteerSession.get().isTaxEnabled()) {
              taxCoefficient =
                  BigDecimal.ONE.add(
                      item.getModelObject()
                          .getTaxRate()
                          .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_DOWN));
            }
            BookmarkablePageLink<EditContractPage> link =
                new BookmarkablePageLink<EditContractPage>(
                    "editContract",
                    ContractDetailsPage.class,
                    EditContractPage.createParameters(contractId));
            link.add(
                new Label("contractName", item.getModel().map(ContractBaseData::getContractName)));
            item.add(link);
            item.add(
                new Label(
                    "internalNumber", item.getModel().map(ContractBaseData::getInternalNumber)));
            item.add(
                new DateLabel("startDate", item.getModel().map(ContractBaseData::getStartDate)));
            item.add(
                new EnumLabel<ContractEntity.ContractType>(
                    "type", item.getModel().map(ContractBaseData::getType)));
            item.add(
                new ListView<DynamicAttributeField>(
                    "contractRow", item.getModel().map(ContractBaseData::getContractAttributes)) {
                  @Override
                  protected void populateItem(ListItem<DynamicAttributeField> item) {
                    item.add(new Label("contractRowText", item.getModelObject().getValue()));
                  }
                });
            item.add(
                new Label(
                    "budgetTotal",
                    Model.of(
                        MoneyUtil.toDouble(
                            item.getModelObject().getBudget(),
                            BudgeteerSession.get().getSelectedBudgetUnit(),
                            taxCoefficient.doubleValue()))));
            item.add(
                new Label(
                    "budgetSpent",
                    Model.of(
                        MoneyUtil.toDouble(
                            item.getModelObject().getBudgetSpent(),
                            BudgeteerSession.get().getSelectedBudgetUnit(),
                            taxCoefficient.doubleValue()))));
            item.add(
                new Label(
                    "budgetLeft",
                    Model.of(
                        MoneyUtil.toDouble(
                            item.getModelObject().getBudgetLeft(),
                            BudgeteerSession.get().getSelectedBudgetUnit(),
                            taxCoefficient.doubleValue()))));
            item.add(
                new BookmarkablePageLink(
                    "editLink",
                    EditContractPage.class,
                    EditContractPage.createParameters(contractId)));
          }
        });

    table.add(
        new ListView<String>("footerRow", Model.ofList(data.getFooter())) {
          @Override
          protected void populateItem(ListItem<String> item) {
            item.add(new Label("footerItem", item.getModelObject()));
          }
        });

    addTableSummaryLabels(table, Model.ofList(data.getContracts()));
    add(table);
  }

  private void addTableSummaryLabels(
      WebMarkupContainer table, IModel<List<ContractBaseData>> model) {

    IModel<ContractTotalData> totalModel = new TotalContractDetailsModel(model);

    // Fill up the columns which contain the contract attributes with empty cells
    RepeatingView repeatingView = new RepeatingView("contractAttributeCell");
    for (int i = 0; i < ((TotalContractDetailsModel) totalModel).getContractAttributeSize(); i++) {
      repeatingView.add(new Label(repeatingView.newChildId(), ""));
    }

    table.add(repeatingView);

    table.add(
        new MoneyLabel(
            "totalAmount",
            new TaxBudgetUnitMoneyModel(
                new BudgetUnitMoneyModel(totalModel.map(ContractTotalData::getBudget)),
                new BudgetUnitMoneyModel(totalModel.map(ContractTotalData::getBudgetGross)))));
    table.add(
        new MoneyLabel(
            "totalSpent",
            new TaxBudgetUnitMoneyModel(
                new BudgetUnitMoneyModel(totalModel.map(ContractTotalData::getBudgetSpent)),
                new BudgetUnitMoneyModel(totalModel.map(ContractTotalData::getBudgetSpentGross)))));
    table.add(
        new MoneyLabel(
            "totalRemaining",
            new TaxBudgetUnitMoneyModel(
                new BudgetUnitMoneyModel(totalModel.map(ContractTotalData::getBudgetLeft)),
                new BudgetUnitMoneyModel(totalModel.map(ContractTotalData::getBudgetLeftGross)))));
  }

  private void createNetGrossLabels(WebMarkupContainer table) {
    table.add(
        new Label(
            "totalLabel",
            new TaxLabelModel(
                new StringResourceModel("overview.table.contract.label.total", this))));
    table.add(
        new Label(
            "leftLabel",
            new TaxLabelModel(
                new StringResourceModel("overview.table.contract.label.left", this))));
    table.add(
        new Label(
            "spentLabel",
            new TaxLabelModel(
                new StringResourceModel("overview.table.contract.label.spent", this))));
  }
}
