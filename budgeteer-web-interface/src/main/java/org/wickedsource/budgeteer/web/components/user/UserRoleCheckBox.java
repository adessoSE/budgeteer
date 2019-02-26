package org.wickedsource.budgeteer.web.components.user;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.pages.administration.ProjectAdministrationPage;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

import java.time.Year;

public class UserRoleCheckBox extends AjaxCheckBox {

    @SpringBean
    private UserService userService;

    private User user;
    private long projectID;

    public UserRoleCheckBox(String id, User user, long projectID) {
        super(id);
        this.user = user;
        this.projectID = projectID;
    }

    public UserRoleCheckBox(String id, User user, long projectID, IModel<Boolean> model) {
        super(id, model);
        this.user = user;
        this.projectID = projectID;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        User thisUser = BudgeteerSession.get().getLoggedInUser();
        boolean checked = getModelObject().booleanValue();

        //Check if the user is losing admin privileges and ask if they are sure
        if (user.getId() == thisUser.getId() &&
                checked &&
                user.getRoles(projectID).contains(UserRole.ADMIN)) {
            setResponsePage(
                    new DeleteDialog() {
                        @Override
                        protected void onYes() {
                            userService.removeAllRolesFromUser(user.getId(), projectID);
                            userService.addRoleToUser(user.getId(), projectID, UserRole.USER);
                            if (user.getId() == thisUser.getId()) {
                                BudgeteerSession.get().setLoggedInUser(user);
                            }
                            setResponsePage(DashboardPage.class);
                        }

                        @Override
                        protected void onNo() {
                            setResponsePage(ProjectAdministrationPage.class);
                        }

                        @Override
                        protected String confirmationText() {
                            return UserRoleCheckBox.this.getString("lose.adminship");
                        }
                    });
        } else {
            userService.removeAllRolesFromUser(user.getId(), projectID);
            userService.addRoleToUser(user.getId(), projectID, UserRole.USER);

            if (checked) {
                userService.addRoleToUser(user.getId(), projectID, UserRole.ADMIN);
            }
        }

        if (user.getId() == thisUser.getId()) {
            BudgeteerSession.get().setLoggedInUser(user);
        }
        target.add(getPage());
    }
}