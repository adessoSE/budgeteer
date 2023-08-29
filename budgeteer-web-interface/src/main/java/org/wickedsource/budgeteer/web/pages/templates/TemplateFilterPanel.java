package org.wickedsource.budgeteer.web.pages.templates;

import java.util.Arrays;
import java.util.HashMap;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.service.ReportType;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class TemplateFilterPanel extends GenericPanel<TemplateFilter> {

  public TemplateFilterPanel(String id, IModel<TemplateFilter> model) {
    super(id, model);
    var form = new Form<>("filterForm", model);
    form.add(createTypeFilter());
    add(form);
  }

  private WebMarkupContainer createTypeFilter() {
    WebMarkupContainer container = new WebMarkupContainer("typesFilterContainer");

    var chosenTypes = getModel().map(TemplateFilter::getTypesList);
    var possibleTypesFromFilter = getModel().map(TemplateFilter::getPossibleTypes);
    var possibleTypes =
        possibleTypesFromFilter.getObject().isEmpty()
            ? Model.ofList(Arrays.asList(ReportType.values()))
            : possibleTypesFromFilter;
    var selectedTypes =
        new ListMultipleChoice<>(
            "typesSelect",
            chosenTypes,
            possibleTypes,
            new AbstractChoiceRenderer<>() {
              @Override
              public Object getDisplayValue(ReportType object) {
                return object.toString();
              }
            });
    selectedTypes.add(
        new AjaxFormComponentUpdatingBehavior("change") {
          @Override
          protected void onUpdate(AjaxRequestTarget target) {
            BudgeteerSession.get()
                .setTemplateFilter((TemplateFilter) getDefaultModel().getObject());
            send(getPage(), Broadcast.BREADTH, getDefaultModelObject());
          }
        });
    selectedTypes.setRequired(false);
    HashMap<String, String> options = MultiselectBehavior.getRecommendedOptions();
    options.put("buttonWidth", "'160px'");
    options.put("includeSelectAllOption", "false");
    options.remove("buttonClass");
    options.put("enableFiltering", "false");
    options.put("enableCaseInsensitiveFiltering", "false");
    selectedTypes.add(new MultiselectBehavior(options));
    container.add(selectedTypes);
    return container;
  }
}
