package org.wickedsource.budgeteer.web.components.user;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;
import org.wickedsource.budgeteer.web.pages.administration.ProjectAdministrationPage;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserRoleDropdown extends Panel {

    @SpringBean
    private UserService userService;

    public UserRoleDropdown(String id, User user, long projectID) {
        super(id);
        List<UserRole> choices = Arrays.asList(UserRole.values());
        User thisUser = BudgeteerSession.get().getLoggedInUser();
        ListMultipleChoice<UserRole> dropdown = new ListMultipleChoice<>("roleDropdown", new Model<>(
                new ArrayList<>(user.getRoles(projectID))), choices);
        HashMap<String, String> options = new HashMap<>();
        options.clear();
        options.put("buttonWidth","'120px'");
        dropdown.add(new MultiselectBehavior(options));
        dropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(!dropdown.getModelObject().isEmpty()) {

                    //Check if the user is losing admin privileges and ask if they are sure
                    if(user.getId() == thisUser.getId() &&
                            !dropdown.getModelObject().contains(UserRole.ADMIN)
                            && user.getRoles(projectID).contains(UserRole.ADMIN)){
                        setResponsePage(
                                new DeleteDialog() {
                                    @Override
                                    protected void onYes() {
                                        userService.removeAllRolesFromUser(user.getId(), projectID);
                                        for (UserRole e : dropdown.getModelObject()) {
                                            userService.addRoleToUser(user.getId(), projectID, e);
                                        }
                                        if(user.getId() == thisUser.getId()){
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
                                        return UserRoleDropdown.this.getString("lose.adminship");
                                    }
                                });
                    }else {
                        userService.removeAllRolesFromUser(user.getId(), projectID);
                        for (UserRole e : dropdown.getModelObject()) {
                            userService.addRoleToUser(user.getId(), projectID, e);
                        }
                    }
                }
                if(user.getId() == thisUser.getId()){
                    BudgeteerSession.get().setLoggedInUser(user);
                }
                target.add(getPage());
            }
        });
        add(dropdown);
    }
}
