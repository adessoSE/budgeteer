package org.wickedsource.budgeteer.web.pages.contract.overview.table;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.pages.contract.details.ContractDetailsPage;
import org.wickedsource.budgeteer.web.pages.contract.edit.EditContractPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class ContractOverviewTable extends Panel{

    @SpringBean
    private ContractService contractService;

    public ContractOverviewTable(String id) {
        super(id);
        ContractOverviewTableModel data = contractService.getContractOverviewByProject(BudgeteerSession.get().getProjectId());
        WebMarkupContainer table = new WebMarkupContainer("table");
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
                BookmarkablePageLink<EditContractPage> link = new BookmarkablePageLink<EditContractPage>("editContract", ContractDetailsPage.class, EditContractPage.createParameters(item.getModelObject().getContractId()));
                link.add(new Label("contractName", model(from(item.getModelObject()).getContractName())));
                item.add(link);
                item.add(new Label("internalNumber", model(from(item.getModelObject()).getInternalNumber())));
                item.add(new Label("year", model(from(item.getModelObject()).getYear())));
                item.add(new EnumLabel<ContractEntity.ContractType>("type", model(from(item.getModelObject()).getType())));
                item.add(new ListView<DynamicAttributeField>("contractRow", model(from(item.getModelObject()).getContractAttributes())) {
                    @Override
                    protected void populateItem(ListItem<DynamicAttributeField> item) {
                        item.add(new Label("contractRowText", item.getModelObject().getValue()));
                    }
                });
                item.add(new Label("budget", model(from(item.getModelObject()).getBudget())));
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
}
