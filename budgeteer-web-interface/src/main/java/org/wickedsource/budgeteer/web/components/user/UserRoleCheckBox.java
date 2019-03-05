package org.wickedsource.budgeteer.web.components.user;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.administration.ProjectAdministrationPage;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

import java.util.Set;

public class UserRoleCheckBox extends Panel {

    @SpringBean
    private UserService userService;

    private User user;
    private long projectID;
    private Boolean isAdmin;
    private User thisUser;

    public UserRoleCheckBox(String id, User user, long projectID) {
        super(id);
        this.user = user;
        this.projectID = projectID;

        thisUser = BudgeteerSession.get().getLoggedInUser();
        isAdmin = user.isProjectAdmin(projectID);
        CheckBox checkBox = new CheckBox("roleCheckBox", new Model<>(isAdmin)) {
            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("userId", String.valueOf(user.getId()));
            }
        };

        // AJAX handler for post requests from JavaScript
        final AbstractDefaultAjaxBehavior behave = new AbstractDefaultAjaxBehavior() {
            @Override
            protected void respond(AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getQueryParameters();

                Set<String> parameterNames = params.getParameterNames();
                String sentID = parameterNames.toArray()[1].toString();
                long extractedID = Long.parseLong(sentID);
                boolean checked = Boolean.parseBoolean(params.getParameterValue(sentID).toString());

                if (extractedID == user.getId()) {
                    changeAdminState(checked);
                }
                else {
                    error(getString("checkbox.error"));
                }

                target.add(getPage());
            }

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                String componentMarkupId = component.getMarkupId();
                String callbackUrl = getCallbackUrl().toString();
                response.render(JavaScriptHeaderItem.forScript("var componentMarkupId='" + componentMarkupId + "'; var callbackUrl"+user.getId()+"='" + callbackUrl + "';", String.valueOf(user.getId())));
            }
        };

        add(behave);

        this.add(new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                response.render(OnDomReadyHeaderItem.forScript(
                        "$(\"input[userId=" + user.getId() + "]\").on('ifChanged', function (e) {\n" +
                                "   id = $(this).attr(\"userId\");\n" +
                                "   checked = $(this).is(\":checked\");\n" +
                                "   var commandToSend = id+'='+checked;\n" +
                                "   var wcall = Wicket.Ajax.post({\n" +
                                "       u: callbackUrl"+user.getId()+"+'&'+commandToSend\n" +
                                "   });\n" +
                                "   $(this).trigger(\"change\", e);\n" +
                                "});"));
            }
        });

        add(checkBox);
    }

    void changeAdminState(boolean checked) {
        isAdmin = checked;
        if (user.getId() == thisUser.getId() &&
                !checked && user.isProjectAdmin(projectID)) {
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

            success(getString("checkbox.success"));
        }

        if (user.getId() == thisUser.getId()) {
            BudgeteerSession.get().setLoggedInUser(user);
        }
    }
}