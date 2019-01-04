package de.adesso.budgeteer.web.pages.person.edit;

import de.adesso.budgeteer.service.person.PersonService;
import de.adesso.budgeteer.service.person.PersonWithRates;
import de.adesso.budgeteer.web.components.notificationlist.PersonNotificationListPanel;
import de.adesso.budgeteer.web.pages.person.edit.personrateform.EditPersonForm;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Strategy to be used by EditPersonPage to UPDATE an existing person.
 */
public class UpdateStrategy implements IEditPersonPageStrategy {

    @SpringBean
    private PersonService service;

    private EditPersonPage page;

    public UpdateStrategy(EditPersonPage page) {
        Injector.get().inject(this);
        this.page = page;
    }

    @Override
    public Label createPageTitleLabel(String id) {
        return new Label(id, new StringResourceModel("page.title.editmode", page, null));
    }

    @Override
    public Label createSubmitButtonLabel(String id) {
        return new Label(id, new StringResourceModel("button.save.editmode", page, null));
    }

    @Override
    public Panel createNotificationList(String id, long personId) {
        return new PersonNotificationListPanel(id, new PersonNotificationsModel(personId));
    }

    @Override
    public EditPersonForm createForm(String id, long personId) {
        PersonWithRates person = service.loadPersonWithRates(personId);
        return new EditPersonForm(id, person, this);
    }

}
