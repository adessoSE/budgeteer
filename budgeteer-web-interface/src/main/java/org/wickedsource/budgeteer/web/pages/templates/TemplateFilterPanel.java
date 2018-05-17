package org.wickedsource.budgeteer.web.pages.templates;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.ReportType;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;
import org.wicketstuff.lazymodel.LazyModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class TemplateFilterPanel extends Panel {

    private boolean typeFilterEnabled = true;

    @SuppressWarnings("unchecked")
    public TemplateFilterPanel(String id, TemplateFilter filter) {
        super(id, model(from(filter)));
        IModel<TemplateFilter> model = (IModel<TemplateFilter>) getDefaultModel();
        Form<TemplateFilter> form = new Form<TemplateFilter>("filterForm", model);
        form.add(createTypeFilter("typesFilterContainer", form));
        add(form);
    }

    private WebMarkupContainer createTypeFilter(String id, Form<TemplateFilter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isTemplateFilterEnabled();
            }
        };

        LazyModel<List<ReportType>> chosenTypes = model(from(form.getModelObject().getTypesList()));
        List<ReportType> possibleTypesFromFilter = form.getModelObject().getPossibleTypes();
        List<ReportType> possibleTypes = possibleTypesFromFilter.isEmpty() ? Arrays.asList(ReportType.values()) : possibleTypesFromFilter;
        ListMultipleChoice<ReportType> selectedTypes = new ListMultipleChoice<>("typesSelect", chosenTypes,
                possibleTypes, new AbstractChoiceRenderer<ReportType>() {
            @Override
            public Object getDisplayValue(ReportType object) {
                return object.toString();
            }
        });
        selectedTypes.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                BudgeteerSession.get().setTemplateFilter((TemplateFilter)getDefaultModel().getObject());
                send(getPage(), Broadcast.BREADTH, getDefaultModelObject());
            }
        });
        selectedTypes.setRequired(false);
        HashMap<String, String> options = MultiselectBehavior.getRecommendedOptions();
        options.put("buttonWidth","'160px'");
        options.put("includeSelectAllOption","false");
        options.remove("buttonClass");
        options.put("enableFiltering","false");
        options.put("enableCaseInsensitiveFiltering", "false");
        selectedTypes.add(new MultiselectBehavior(options));
        container.add(selectedTypes);
        return container;
    }

    public boolean isTemplateFilterEnabled() {
        return typeFilterEnabled;
    }

    public void setTemplateFilterEnabled(boolean typeFilterEnabled) {
        this.typeFilterEnabled = typeFilterEnabled;
    }

}