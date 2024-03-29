package org.wickedsource.budgeteer.web.components.aggregatedrecordtable;

import java.util.List;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.components.money.BudgetUnitMoneyModel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.components.tax.TaxBudgetUnitMoneyModel;
import org.wickedsource.budgeteer.web.components.tax.TaxLabelModel;

public class AggregatedRecordTable extends Panel {

  public AggregatedRecordTable(String id, IModel<List<AggregatedRecord>> model) {
    super(id, model);
    setRenderBodyOnly(true);
    WebMarkupContainer table = new WebMarkupContainer("hoursTable");
    table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));

    createNetGrossLabels(table);

    table.add(createList("list"));
    add(table);
  }

  @SuppressWarnings("unchecked")
  private ListView<AggregatedRecord> createList(String id) {
    return new ListView<AggregatedRecord>(id, (IModel<List<AggregatedRecord>>) getDefaultModel()) {
      @Override
      protected void populateItem(ListItem<AggregatedRecord> item) {
        item.add(
            new Label("title", item.getModel().map(AggregatedRecord::getAggregationPeriodTitle)));
        item.add(
            new Label(
                "startDate", item.getModel().map(AggregatedRecord::getAggregationPeriodStart)));
        item.add(
            new Label("endDate", item.getModel().map(AggregatedRecord::getAggregationPeriodEnd)));
        item.add(new Label("hours", item.getModel().map(AggregatedRecord::getHours)));

        item.add(
            new MoneyLabel(
                "budgetBurned_net",
                new TaxBudgetUnitMoneyModel(
                    new BudgetUnitMoneyModel(
                        item.getModel().map(AggregatedRecord::getBudgetBurned_net)),
                    new BudgetUnitMoneyModel(
                        item.getModel().map(AggregatedRecord::getBudgetBurned_gross)))));

        item.add(
            new MoneyLabel(
                "budgetPlanned_net",
                new TaxBudgetUnitMoneyModel(
                    new BudgetUnitMoneyModel(
                        item.getModel().map(AggregatedRecord::getBudgetPlanned_net)),
                    new BudgetUnitMoneyModel(
                        item.getModel().map(AggregatedRecord::getBudgetPlanned_gross)))));

        Label differenceLabel =
            new MoneyLabel(
                "difference",
                new BudgetUnitMoneyModel(
                    item.getModel()
                        .map(
                            aggregatedRecord ->
                                BudgeteerSession.get().isTaxEnabled()
                                    ? aggregatedRecord.getDifference_gross()
                                    : aggregatedRecord.getDifference()))) {
              // Label differenceLabel = new MoneyLabel("difference", new
              // BudgetUnitMoneyModel(model(from(item.getModel()).getDifference()))) {
              @Override
              protected void onConfigure() {
                super.onConfigure();
                IModel<Money> model = (IModel<Money>) getDefaultModel();
                if (model.getObject().isPositive()) {
                  add(new AttributeAppender("class", "text-green"));
                } else if (model.getObject().isNegative()) {
                  add(new AttributeAppender("class", "text-red"));
                }
              }
            };
        item.add(differenceLabel);
      }

      @Override
      protected ListItem<AggregatedRecord> newItem(int index, IModel<AggregatedRecord> itemModel) {
        return super.newItem(
            index,
            new ClassAwareWrappingModel<AggregatedRecord>(itemModel, AggregatedRecord.class));
      }
    };
  }

  private void createNetGrossLabels(WebMarkupContainer table) {
    table.add(
        new Label(
            "plannedLabel",
            new TaxLabelModel(
                new StringResourceModel("aggregated.table.budget.plannedLabel", this))));
    table.add(
        new Label(
            "burnedLabel",
            new TaxLabelModel(
                new StringResourceModel("aggregated.table.budget.burnedLabel", this))));
    table.add(
        new Label(
            "differenceLabel",
            new TaxLabelModel(
                new StringResourceModel("aggregated.table.budget.differenceLabel", this))));
  }
}
