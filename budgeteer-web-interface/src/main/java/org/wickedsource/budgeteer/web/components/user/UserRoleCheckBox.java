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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;

import java.util.Set;

public class UserRoleCheckBox extends Panel {

    @SpringBean
    private UserService userService;

    private User user;
    private long projectID;
    private Boolean isAdmin;
    private User thisUser;
    private Class originPage;
    private Class afterAdminDeletionPage;
    private PageParameters pageParameters;

    public UserRoleCheckBox(String id, User user, long projectID, CustomFeedbackPanel feedbackPanel, UserRoleTable table,
                            Class originPage, Class afterAdminDeletionPage, PageParameters pageParameters) {
        super(id);
        this.user = user;
        this.projectID = projectID;
        this.originPage = originPage;
        this.afterAdminDeletionPage = afterAdminDeletionPage;
        this.pageParameters = pageParameters;

        thisUser = BudgeteerSession.get().getLoggedInUser();
        isAdmin = user.isProjectAdmin(projectID);
        CheckBox checkBox = new CheckBox("roleCheckBox", new Model<>(isAdmin)) {
            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("userId", String.valueOf(user.getId()));
            }
        };

        final AbstractDefaultAjaxBehavior afterCheckBoxClickBehavior = new AbstractDefaultAjaxBehavior() {
            String sentID;

            @Override
            protected void respond(AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getQueryParameters();

                if (sendParametersAreCorrect(params)) {
                    changeAdminState(isChecked(params));
                } else {
                    error(getString("checkbox.error"));
                }

                target.add(feedbackPanel);
                target.add(table);

                // Call the iCheck-Plugin, so the style of the checkbox stays the same
                target.appendJavaScript("$('input').iCheck({\n" +
                        "    checkboxClass: 'icheckbox_minimal',\n" +
                        "    radioClass: 'iradio_minimal',\n" +
                        "    increaseArea: '20%'\n" +
                        "  });");
            }

            private boolean sendParametersAreCorrect(IRequestParameters params) {
                Set<String> parameterNames = params.getParameterNames();
                sentID = parameterNames.toArray()[1].toString();
                long extractedID = Long.parseLong(sentID);

                return extractedID == user.getId();
            }

            private boolean isChecked(IRequestParameters params) {
                return Boolean.parseBoolean(params.getParameterValue(sentID).toString());
            }

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                String componentMarkupId = component.getMarkupId();
                String callbackUrl = getCallbackUrl().toString();
                response.render(JavaScriptHeaderItem.forScript("var componentMarkupId='" + componentMarkupId + "'; var callbackUrl" + user.getId() + "='" + callbackUrl + "';", String.valueOf(user.getId())));
            }
        };

        add(afterCheckBoxClickBehavior);

        // send a request via AJAX when the checkbox is clicked
        add(new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                response.render(OnDomReadyHeaderItem.forScript(
                        "$('input[userId=" + user.getId() + "]').on('ifChanged', function (e) {\n" +
                                "sendAJAX" + user.getId() + "(this);\n" +
                                "});\n" +
                                "\n" +
                                "$('input[userId=" + user.getId() + "]').change(function(){\n" +
                                "sendAJAX" + user.getId() + "(this);\n" +
                                "});\n" +
                                "\n" +
                                "function sendAJAX" + user.getId() + "(checkBox){\n" +
                                "id = $(checkBox).attr('userId');\n" +
                                "checked = $(checkBox).is(':checked');\n" +
                                "var commandToSend = id+'='+checked;\n" +
                                " var wcall = Wicket.Ajax.get({\n" +
                                "u: callbackUrl" + user.getId() + "+'&'+commandToSend\n" +
                                "});\n" +
                                "}"));
            }
        });

        add(checkBox);
    }

    private void changeAdminState(boolean checked) {
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
                                user = userService.getUpdatedUser(user);
                                BudgeteerSession.get().setLoggedInUser(user);
                            }
                            setResponsePage(afterAdminDeletionPage, pageParameters);
                        }

                        @Override
                        protected void onNo() {
                            setResponsePage(originPage, pageParameters);
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
            user = userService.getUpdatedUser(user);
        }
    }
}