package de.adesso.budgeteer.web.pages.person.edit;

import de.adesso.budgeteer.service.notification.Notification;
import de.adesso.budgeteer.service.notification.NotificationService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class PersonNotificationsModel extends LoadableDetachableModel<List<Notification>> {

    @SpringBean
    private NotificationService service;

    private long personId;

    public PersonNotificationsModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected List<Notification> load() {
        return service.getNotificationsForPerson(personId);
    }
}
