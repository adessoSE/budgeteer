package org.wickedsource.budgeteer.web.pages.person.overview.table;

import org.apache.wicket.markup.html.WebPage;
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
import org.wickedsource.budgeteer.web.pages.person.edit.EditPersonPage;
import org.wickedsource.budgeteer.web.pages.person.overview.PeopleOverviewPage;

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
                final PersonBaseData modelObject = item.getModelObject();
                final PageParameters parameters = PersonDetailsPage.createParameters(modelObject.getId());
                Link link = new BookmarkablePageLink<PersonDetailsPage>("personLink", PersonDetailsPage.class, parameters);
                link.add(new Label("personName", modelObject.getName()));
                item.add(link);
                item.add(new MoneyLabel("dailyRate", model(from(modelObject).getAverageDailyRate())));
                item.add(new Label("lastBookedDate", modelObject.getLastBooked()));

                Link editPersonLink = new Link("editPage") {
                    @Override
                    public void onClick() {
                        WebPage page = new EditPersonPage(EditPersonPage.createParameters(modelObject.getId()), PeopleOverviewPage.class, null);
                        setResponsePage(page);
                    }
                };
                item.add(editPersonLink);

            }
        };
    }

}
