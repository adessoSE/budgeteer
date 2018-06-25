package org.wickedsource.budgeteer.web.pages.contract.overview.table;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.components.datelabel.DateLabel;
import org.wickedsource.budgeteer.web.components.tax.TaxLabelModel;
import org.wickedsource.budgeteer.web.pages.contract.details.ContractDetailsPage;
import org.wickedsource.budgeteer.web.pages.contract.edit.EditContractPage;

public class ContractOverviewTable extends Panel{

	@SpringBean
	private ContractService contractService;


	public ContractOverviewTable(String id) {
		super(id);
		ContractOverviewTableModel data = contractService.getContractOverviewByProject(BudgeteerSession.get().getProjectId());
		WebMarkupContainer table = new WebMarkupContainer("table");

		createNetGrossLabels(table);

		table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));
		table.add(new ListView<String>("headerRow",  model(from(data).getHeadline()) ) {
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("headerItem", item.getModelObject()));
			}
		});
		table.add(new ListView<ContractBaseData>("contractRows", model(from(data).getContracts())) {
			@Override
			protected void populateItem(ListItem<ContractBaseData> item) {
				long contractId = item.getModelObject().getContractId();
				double taxCoefficient = 1.0;

				if (BudgeteerSession.get().isTaxEnabled()) {
					taxCoefficient = 1.0 + item.getModelObject().getTaxRate() / 100.0;
				}
				BookmarkablePageLink<EditContractPage> link = new BookmarkablePageLink<EditContractPage>("editContract",
						ContractDetailsPage.class, EditContractPage.createParameters(contractId));
				link.add(new Label("contractName", model(from(item.getModelObject()).getContractName())));
				item.add(link);
				item.add(new Label("internalNumber", model(from(item.getModelObject()).getInternalNumber())));
				item.add(new DateLabel("startDate", model(from(item.getModelObject()).getStartDate())));
				item.add(new EnumLabel<ContractEntity.ContractType>("type", model(from(item.getModelObject()).getType())));
				item.add(new ListView<DynamicAttributeField>("contractRow", model(from(item.getModelObject()).getContractAttributes())) {
					@Override
					protected void populateItem(ListItem<DynamicAttributeField> item) {
						item.add(new Label("contractRowText", item.getModelObject().getValue()));
					}
				});
				item.add(new Label("budgetTotal", Model.of(MoneyUtil.toDouble(item.getModelObject().getBudget(),
						BudgeteerSession.get().getSelectedBudgetUnit(), taxCoefficient))));
				item.add(new Label("budgetSpent", Model.of(MoneyUtil.toDouble(item.getModelObject().getBudgetSpent(),
						BudgeteerSession.get().getSelectedBudgetUnit(), taxCoefficient))));
				item.add(new Label("budgetLeft", Model.of(MoneyUtil.toDouble(item.getModelObject().getBudgetLeft(),
						BudgeteerSession.get().getSelectedBudgetUnit(), taxCoefficient))));
				item.add(new BookmarkablePageLink("editLink", EditContractPage.class,
						EditContractPage.createParameters(contractId)));
			}
		});
		table.add(new ListView<String>("footerRow", model(from(data).getFooter()) ) {
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("footerItem", item.getModelObject()));
			}
		});
		add(table);
	}

	private void createNetGrossLabels(WebMarkupContainer table) {
		table.add(new Label("totalLabel", new TaxLabelModel(
				new StringResourceModel("overview.table.contract.label.total", this))));
		table.add(new Label("leftLabel", new TaxLabelModel(
				new StringResourceModel("overview.table.contract.label.left", this))));
		table.add(new Label("spentLabel", new TaxLabelModel(
				new StringResourceModel("overview.table.contract.label.spent", this))));
	}
}