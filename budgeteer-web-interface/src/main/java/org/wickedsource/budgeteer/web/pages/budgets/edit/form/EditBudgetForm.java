package org.wickedsource.budgeteer.web.pages.budgets.edit.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.EditBudgetData;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationListPanel;
import org.wickedsource.budgeteer.web.pages.budgets.BudgetTagsModel;
import org.wickedsource.budgeteer.web.pages.budgets.edit.tagsfield.TagsTextField;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditBudgetForm extends Form<EditBudgetData> {

    @SpringBean
    private BudgetService service;

    public EditBudgetForm(String id){
        super(id, new ClassAwareWrappingModel<EditBudgetData>(Model.of(new EditBudgetData(BudgeteerSession.get().getProjectId())), EditBudgetData.class));
        addComponents();
    }


    public EditBudgetForm(String id, IModel<EditBudgetData> model) {
        super(id, model);
        Injector.get().inject(this);
        addComponents();
    }

    private void addComponents() {
        TagsTextField tagsField = new TagsTextField("tagsInput", model(from(getModel()).getTags()));
        tagsField.setOutputMarkupId(true);
        add(tagsField);
        add(new CustomFeedbackPanel("feedback"));
        add(new RequiredTextField<String>("name", model(from(getModel()).getTitle())));
        add(new RequiredTextField<String>("importKey", model(from(getModel()).getImportKey())));
        MoneyTextField totalField = new MoneyTextField("total", model(from(getModel()).getTotal()));
        totalField.setRequired(true);
        add(totalField);
        add(createTagsList("tagsList", new BudgetTagsModel(BudgeteerSession.get().getProjectId()), tagsField));
        add(new NotificationListPanel("notificationList", new BudgetNotificationsModel(getModel().getObject().getId())));
    }

    private ListView<String> createTagsList(String id, IModel<List<String>> model, final Component tagsField) {
        return new ListView<String>(id, model) {
            @Override
            protected void populateItem(final ListItem<String> item) {
                Label label = new Label("tag", model(from(item.getModel())));
                label.setRenderBodyOnly(true);
                item.add(label);
                item.add(new AjaxEventBehavior("click") {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        ((List<String>) tagsField.getDefaultModelObject()).add(item.getModelObject());
                        target.appendJavaScript(String.format("$('#%s').tagsinput('add', '%s');", tagsField.getMarkupId(), item.getModelObject()));
                    }
                });
            }

            @Override
            protected ListItem<String> newItem(int index, IModel<String> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<String>(itemModel, String.class));
            }
        };
    }

    @Override
    protected void onSubmit() {
            service.saveBudget(getModelObject());
            this.success(getString("feedback.success"));
    }
}
