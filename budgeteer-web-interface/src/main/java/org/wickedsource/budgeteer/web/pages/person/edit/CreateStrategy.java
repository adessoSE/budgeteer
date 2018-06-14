package org.wickedsource.budgeteer.web.pages.person.edit;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.wickedsource.budgeteer.web.components.notificationlist.EmptyNotificationsModel;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationListPanel;
import org.wickedsource.budgeteer.web.pages.person.edit.personrateform.EditPersonForm;

/**
 * Strategy to be used by EditPersonPage to CREATE a new person.
 */
public class CreateStrategy implements IEditPersonPageStrategy {

    private EditPersonPage page;

    public CreateStrategy(EditPersonPage page) {
        this.page = page;
    }

    @Override
    public Label createPageTitleLabel(String id) {
        return new Label(id, new StringResourceModel("page.title.createmode", page, null));
    }

    @Override
    public Label createSubmitButtonLabel(String id) {
        return new Label(id, new StringResourceModel("button.save.createmode", page, null));
    }

    @Override
    public Panel createNotificationList(String id, long personId) {
        return new NotificationListPanel(id, new EmptyNotificationsModel());
    }

    @Override
    public EditPersonForm createForm(String id, long personId) {
        return new EditPersonForm(id, this);
    }

}
