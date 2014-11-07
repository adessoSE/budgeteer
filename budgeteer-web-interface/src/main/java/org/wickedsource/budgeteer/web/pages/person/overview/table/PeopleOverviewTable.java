package org.wickedsource.budgeteer.web.pages.person.overview.table;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.person.PersonBaseData;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.pages.person.details.PersonDetailsPage;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;


public class PeopleOverviewTable extends Panel {

    public PeopleOverviewTable(String id, IModel<List<PersonBaseData>> model) {
        super(id, model);
        setRenderBodyOnly(true);
        add(createPersonList("personList", model));
    }

    private ListView<PersonBaseData> createPersonList(String id, IModel<List<PersonBaseData>> model) {
        return new ListView<PersonBaseData>(id, model) {
            @Override
            protected void populateItem(ListItem<PersonBaseData> item) {
                PageParameters parameters = PersonDetailsPage.createParameters(item.getModelObject().getId());
                Link link = new BookmarkablePageLink<PersonDetailsPage>("personLink", PersonDetailsPage.class, parameters);
                item.add(link);
                item.add(new MoneyLabel("dailyRate", model(from(item.getModelObject()).getAverageDailyRate())));
                item.add(new Label("lastBooked", item.getModelObject().getLastBooked()));
            }
        };
    }

}
