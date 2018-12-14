package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.service.user.User;
import de.adesso.budgeteer.service.user.UserService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class UsersInProjectModel extends AbstractReadOnlyModel<List<User>> {

    @SpringBean
    private UserService service;

    private long projectId;

    public UsersInProjectModel(long projectId) {
        Injector.get().inject(this);
        this.projectId = projectId;
    }

    @Override
    public List<User> getObject() {
        return service.getUsersInProject(projectId);
    }
}
