package org.wickedsource.budgeteer.web.pages.budgets.overview.filter;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import java.util.List;

public class BudgetTagFilterPanel extends Panel {

    public BudgetTagFilterPanel(String id, IModel<List<String>> tagsModel, IModel<BudgetTagFilter> filter) {
        super(id, filter);
        Form<BudgetTagFilter> form = new Form<BudgetTagFilter>("filterForm", filter) {
            @Override
            protected void onSubmit() {
                send(getPage(), Broadcast.BREADTH, getFilter());
            }
        };
        form.add(createTagButtonList("tagButtonList", tagsModel));
        form.add(createResetButton("resetButton", tagsModel));
        add(form);
    }

    private Button createResetButton(String id, final IModel<List<String>> tagsModel) {
        return new Button(id) {
            @Override
            public void onSubmit() {
                getFilter().getSelectedTags().clear();
            }

            @Override
            public boolean isVisible() {
                return !tagsModel.getObject().isEmpty();
            }
        };
    }

    private ListView<String> createTagButtonList(String id, IModel<List<String>> tagsModel) {
        return new ListView<String>(id, tagsModel) {
            @Override
            protected void populateItem(final ListItem<String> item) {

                Button button = new Button("button") {
                    @Override
                    public void onSubmit() {
                        getFilter().toggleTag(item.getModelObject());
                    }
                };

                button.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
                    @Override
                    public String getObject() {
                        if (getFilter().isTagSelected(item.getModelObject())) {
                            return " bg-light-blue";
                        } else {
                            return " bg-light-gray";
                        }
                    }
                }));

                Label buttonLabel = new Label("buttonLabel", item.getModelObject());
                buttonLabel.setRenderBodyOnly(true);
                button.add(buttonLabel);
                item.add(button);
                item.setRenderBodyOnly(true);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private BudgetTagFilter getFilter() {
        IModel<BudgetTagFilter> model = (IModel<BudgetTagFilter>) getDefaultModel();
        return model.getObject();
    }

}
