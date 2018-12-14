package de.adesso.budgeteer.web.pages.person.overview.table;

import de.adesso.budgeteer.service.person.PersonBaseData;
import de.adesso.budgeteer.web.pages.person.details.PersonDetailsPage;
import de.adesso.budgeteer.web.pages.person.edit.EditPersonPage;
import de.adesso.budgeteer.web.pages.person.overview.PeopleOverviewPage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import de.adesso.budgeteer.web.components.dataTable.DataTableBehavior;
import de.adesso.budgeteer.web.components.money.MoneyLabel;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;


public class PeopleOverviewTable extends Panel {

    public PeopleOverviewTable(String id, IModel<List<PersonBaseData>> model) {
        super(id, model);
        setRenderBodyOnly(true);
        WebMarkupContainer table = new WebMarkupContainer("table");
        table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));

        table.add(createPersonList("personList", model));
        add(table);
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
                        WebPage page = new EditPersonPage(EditPersonPage.createParameters(modelObject.getId()), PeopleOverviewPage.class, new PageParameters());
                        setResponsePage(page);
                    }
                };
                item.add(editPersonLink);

            }
        };
    }

}
