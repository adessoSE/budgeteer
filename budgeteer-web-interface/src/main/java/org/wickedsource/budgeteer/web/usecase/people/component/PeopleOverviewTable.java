package org.wickedsource.budgeteer.web.usecase.people.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.people.Person;
import org.wickedsource.budgeteer.web.usecase.people.PersonDetailsPage;

import java.util.List;

public class PeopleOverviewTable extends Panel {

    public PeopleOverviewTable(String id, IModel<List<Person>> model) {
        super(id, model);
        setRenderBodyOnly(true);
        add(createPersonList("personList", model));
    }

    private ListView<Person> createPersonList(String id, IModel<List<Person>> model) {
        return new ListView<Person>(id, model) {
            @Override
            protected void populateItem(ListItem<Person> item) {
                PageParameters parameters = new PageParameters();
                parameters.add("id", item.getModelObject().getId());
                Link link = new BookmarkablePageLink<PersonDetailsPage>("personLink", PersonDetailsPage.class);
                item.add(link);
                item.add(new Label("dailyRate", item.getModelObject().getAverageDailyRate()));
                item.add(new Label("lastBooked", item.getModelObject().getLastBooked()));
            }
        };
    }

}
