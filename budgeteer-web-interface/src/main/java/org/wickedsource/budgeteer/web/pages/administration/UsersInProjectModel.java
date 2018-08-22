package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;

import java.util.List;

public class UsersInProjectModel extends AbstractReadOnlyModel<List<User>> {

    @SpringBean
    private UserService service;

    private long projectId;

    UsersInProjectModel(long projectId) {
        Injector.get().inject(this);
        this.projectId = projectId;
    }

    @Override
    public List<User> getObject() {
        return service.getUsersInProject(projectId);
    }
}
