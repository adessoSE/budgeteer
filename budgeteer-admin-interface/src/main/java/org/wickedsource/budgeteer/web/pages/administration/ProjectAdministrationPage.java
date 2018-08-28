package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.BudgeteerSettings;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/administration")
public class ProjectAdministrationPage extends BasePage {

    @SpringBean
    private UserService userService;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private BudgeteerSettings settings;

    public ProjectAdministrationPage() {
        add(new CustomFeedbackPanel("feedback"));
        add(createUserList("userList", new UsersInProjectModel(BudgeteerSession.get().getProjectId())));
    }

    private ListView<User> createUserList(String id, IModel<List<User>> model) {
        User thisUser = BudgeteerSession.get().getLoggedInUser();
        return new ListView<User>(id, model) {
            @Override
            protected void populateItem(final ListItem<User> item) {
                item.add(new Label("username", model(from(item.getModel()).getName())));
                Link deleteButton = new Link("deleteButton") {
                    @Override
                    public void onClick() {

                        setResponsePage(new DeleteDialog(() -> {
                            userService.removeUser(item.getModelObject().getId());
                            setResponsePage(ProjectAdministrationPage.class, getPageParameters());
                            return null;
                        }, () -> {
                            setResponsePage(ProjectAdministrationPage.class, getPageParameters());
                            return null;
                        }));
                    }
                };
                // a user may not delete herself/himself
                if (item.getModelObject().equals(thisUser))
                    deleteButton.setVisible(false);
                item.add(deleteButton);
            }

            @Override
            protected ListItem<User> newItem(int index, IModel<User> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, User.class));
            }
        };
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(ProjectAdministrationPage.class);
    }

}
