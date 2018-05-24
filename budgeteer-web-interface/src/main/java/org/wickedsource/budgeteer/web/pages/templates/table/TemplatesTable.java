package org.wickedsource.budgeteer.web.pages.templates.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.pages.templates.TemplateFilter;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;
import org.wickedsource.budgeteer.web.pages.templates.edit.EditTemplatePage;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class TemplatesTable extends Panel {

    private ListView<Template> rows;
    WebMarkupContainer table;

    @SpringBean
    private TemplateService templateService;

    public TemplatesTable(String id, TemplateListModel model) {
        super(id, model);
        table = new WebMarkupContainer("table");
        table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));
        rows = createList("templateList", model, table);
        rows.setOutputMarkupId(true);
        table.add(rows);
        table.setOutputMarkupId(true);
        this.setOutputMarkupId(true);
        add(table);

        TemplateListModel model2 = (TemplateListModel)TemplatesTable.this.getDefaultModel();
        TemplatesTable.this.setDefaultModel(new TemplateListModel(model2.getFilter()));
    }

    private ListView<Template> createList(String id, final IModel<List<Template>> model, final WebMarkupContainer table) {
        return new ListView<Template>(id, model) {
            @Override
            protected void populateItem(final ListItem<Template> item) {
                item.setOutputMarkupId(true);
                item.add(new Label("name", model(from(item.getModel()).getName())));
                item.add(new Label("description", model(from(item.getModel()).getDescription())));
                item.add(new Label("type", model(from(item.getModel()).getType()).getObject().toString()));
                AjaxCheckBox checkBox = new AjaxCheckBox("setDefault", model(from(item.getModel()).isDefault())) {

                    @Override
                    public void onUpdate(AjaxRequestTarget target){
                        TemplateFormInputDto temp = new TemplateFormInputDto(BudgeteerSession.get().getProjectId());
                        temp.setName(item.getModelObject().getName());
                        temp.setDescription(item.getModelObject().getDescription());
                        temp.setType(item.getModelObject().getType());
                        temp.setDefault(item.getModelObject().isDefault());
                        templateService.editTemplate(temp.getProjectId(), item.getModelObject().getId(), null, model(from(temp)));
                        TemplateListModel model = (TemplateListModel)TemplatesTable.this.getDefaultModel();
                        TemplatesTable.this.setDefaultModel(new TemplateListModel(model.getFilter()));
                        table.detach();
                        RequestCycle.get().find(AjaxRequestTarget.class).add(TemplatesTable.this, TemplatesTable.this.getMarkupId());
                    }
                };
                checkBox.setEnabled(true);
                checkBox.setOutputMarkupId(true);
                item.add(checkBox);

                item.add(new Link("editPage") {
                    @Override
                    public void onClick() {
                        WebPage page = new EditTemplatePage(TemplatesPage.class, getPage().getPageParameters(), item.getModelObject().getId());
                        setResponsePage(page);
                    }
                });}

            @Override
            protected ListItem<Template> newItem(int index, IModel<Template> itemModel) {
                // wrap model to work with LazyModel
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, Template.class));
            }
        };
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        Object payload = event.getPayload();
        if (payload instanceof TemplateFilter) {
            TemplateFilter filter = (TemplateFilter) payload;
            TemplateListModel model = (TemplateListModel) getDefaultModel();
            model.setFilter(filter);
            table.detach();
            RequestCycle.get().find(AjaxRequestTarget.class).add(this, this.getMarkupId());
        }
    }

    public ListView<Template> getRows() {
        return rows;
    }
}
