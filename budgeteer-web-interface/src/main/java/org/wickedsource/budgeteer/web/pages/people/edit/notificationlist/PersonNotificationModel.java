package org.wickedsource.budgeteer.web.pages.people.edit.notificationlist;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.notification.NotificationService;

import java.util.List;

public class PersonNotificationModel extends LoadableDetachableModel<List<Notification>> {

    @SpringBean
    private NotificationService service;

    private long personId;

    public PersonNotificationModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected List<Notification> load() {
        return service.getNotificationsForPerson(personId);
    }
}
