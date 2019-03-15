package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.user.login.LoginPage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.MailAlreadyInUseException;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.service.user.UsernameAlreadyInUseException;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.user.UserRole;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.settings.BudgeteerSettings;

import java.util.List;
import java.util.function.Predicate;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/administartion-menu")
public class BudgeteerAdministrationOverview extends BasePage {

    @SpringBean
    private UserService userService;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private BudgeteerSettings settings;

    private User thisUser = BudgeteerSession.get().getLoggedInUser();

    private CustomFeedbackPanel feedbackPanel = new CustomFeedbackPanel("feedback");

    public BudgeteerAdministrationOverview() {
        add(feedbackPanel);
        add(createUserList("userList", new ListModel<>(userService.getAllUsers())));
        add(createProjectList("projectList", new ListModel<>(projectService.getAllProjects())));
    }

    private ListView<User> createUserList(String id, IModel<List<User>> model) {

        return new ListView<User>(id, model) {
            @Override
            protected void populateItem(final ListItem<User> item) {
                item.add(new Label("username", model(from(item.getModel()).getName())));
                Component deleteUserButton = createDeleteUserButton(item);
                Component setPasswordTextField = createPasswordTextField(item);
                Component makeUserAdmin = createMakeUserAdminButton(item);
                Component revokeAdminRights = createRevokeAdminRightsButton(item);

                //If this is the current user
                if (item.getModelObject().getId() == thisUser.getId()) {
                    deleteUserButton.setVisible(false); //Make the delete button invisible
                    revokeAdminRights.setVisible(false); //The button to revoke rights is hidden
                    if (userService.getAllAdmins().size() > 1) {  // a user may not delete herself/himself unless another admin is present
                        deleteUserButton.setVisible(true);
                        revokeAdminRights.setVisible(true);
                    }
                    makeUserAdmin.setVisible(false);  //We cannot make the user an admin if they already are one
                } else {
                    if (item.getModelObject().getGlobalRole().equals(UserRole.ADMIN)) { //If the user is an admin
                        //We don't check for other admins here since this is not the logged in user (who must be an admin)
                        deleteUserButton.setVisible(true);
                        makeUserAdmin.setVisible(false);
                        revokeAdminRights.setVisible(true);
                    } else {
                        makeUserAdmin.setVisible(true);
                        revokeAdminRights.setVisible(false);
                    }
                }
                item.add(deleteUserButton);
                item.add(setPasswordTextField);
                item.add(createEmailTextField(item));
                item.add(createUsernameTextField(item));
                item.add(makeUserAdmin);
                item.add(revokeAdminRights);
            }

            @Override
            protected ListItem<User> newItem(int index, IModel<User> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, User.class));
            }
        };
    }

    private Component createUsernameTextField(ListItem<User> item) {
        TextField<String> textField = new TextField<>("setUsernameTextBox", Model.of(""));
        Form passwordResetField = new Form("usernameResetField") {
            @Override
            protected void onSubmit() {
                if (textField.getModelObject() == null) {
                    feedbackPanel.error(getString("username.empty"));
                    return;
                }
                try {
                    userService.setUserUsername(item.getModelObject().getId(), textField.getModelObject());
                    item.getModelObject().setName(textField.getModelObject());
                } catch (UsernameAlreadyInUseException e) {
                    feedbackPanel.error(getString("username.in.use"));
                    return;
                }
                feedbackPanel.success(getString("username.success"));
            }
        };
        Button submitButton = new Button("resetUsernameButton", Model.of(BudgeteerAdministrationOverview.this.getString("reset.username")));
        return passwordResetField.add(textField, submitButton);
    }

    private Component createPasswordTextField(ListItem<User> item) {
        PasswordTextField textField = new PasswordTextField("setPasswordTextBox", Model.of(""));
        Form passwordResetField = new Form("passwordResetField") {
            @Override
            protected void onSubmit() {
                userService.setUserPassword(item.getModelObject().getId(), textField.getModelObject());
                feedbackPanel.success(getString("password.success"));
            }
        };
        Button submitButton = new Button("resetPasswordButton", Model.of(BudgeteerAdministrationOverview.this.getString("reset.password")));
        return passwordResetField.add(textField, submitButton);
    }

    private Component createEmailTextField(ListItem<User> item) {
        EmailTextField textField = new EmailTextField("setEmailTextBox", Model.of(item.getModelObject().getMail()));

        Form emailResetField = new Form("emailResetField") {

            @Override
            protected void onSubmit() {
                try {
                    userService.setUserEmail(item.getModelObject().getId(), textField.getModelObject());
                    item.getModelObject().setMail(textField.getModelObject());
                    feedbackPanel.success(getString("email.success"));
                } catch (MailAlreadyInUseException e) {
                    feedbackPanel.error(getString("email.error"));
                }
            }
        };
        Button submitButton = new Button("resetEmailButton", Model.of(BudgeteerAdministrationOverview.this.getString("reset.email")));
        return emailResetField.add(textField, submitButton);
    }

    private Link createDeleteUserButton(ListItem<User> item) {
        return new Link("deleteUserButton") {
            @Override
            public void onClick() {
                setResponsePage(new org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog() {
                    @Override
                    protected void onYes() {
                        if (thisUser.getId() == item.getModelObject().getId()) {
                            userService.removeUser(item.getModelObject().getId());
                            BudgeteerSession.get().logout(); // Log the user out if he deletes himself
                        } else {
                            userService.removeUser(item.getModelObject().getId());
                        }
                        setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                    }

                    @Override
                    protected void onNo() {
                        setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                    }

                    @Override
                    protected String confirmationText() {
                        if (thisUser.getId() == item.getModelObject().getId()) {
                            return new StringResourceModel("remove.self.confirmation", BudgeteerAdministrationOverview.this).getString();
                        } else {
                            return new StringResourceModel("remove.confirmation", BudgeteerAdministrationOverview.this).getString();
                        }
                    }
                });
            }
        };
    }

    private Component createMakeUserAdminButton(ListItem<User> item) {
        return new Link<String>("makeUserAdmin") {
            @Override
            public void onClick() {
                setResponsePage(new org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog() {
                    @Override
                    protected void onYes() {
                        userService.setGlobalRoleForUser(item.getModelObject().getId(), UserRole.ADMIN);
                        item.getModelObject().setGlobalRole(UserRole.ADMIN);
                        setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                    }

                    @Override
                    protected void onNo() {
                        setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                    }

                    @Override
                    protected String confirmationText() {
                        return new StringResourceModel("make.user.admin.confirmation", BudgeteerAdministrationOverview.this).getString();
                    }
                });
            }
        };
    }

    private Component createRevokeAdminRightsButton(ListItem<User> item) {
        return new Link("revokeAdminRights") {
            @Override
            public void onClick() {
                setResponsePage(new org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog() {
                    @Override
                    protected void onYes() {
                        userService.setGlobalRoleForUser(item.getModelObject().getId(), UserRole.USER);
                        item.getModelObject().setGlobalRole(UserRole.USER);
                        if (item.getModelObject().getId() == BudgeteerSession.get().getLoggedInUser().getId()) {
                            setResponsePage(LoginPage.class);
                        } else {
                            setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                        }
                    }

                    @Override
                    protected void onNo() {
                        setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                    }

                    @Override
                    protected String confirmationText() {
                        if (item.getModelObject().getId() == BudgeteerSession.get().getLoggedInUser().getId()) {
                            return new StringResourceModel("lose.adminship", BudgeteerAdministrationOverview.this).getString();
                        } else {
                            return new StringResourceModel("revoke.admin.rights.confirmation", BudgeteerAdministrationOverview.this).getString();
                        }
                    }
                });
            }
        };
    }

    private ListView<ProjectBaseData> createProjectList(String id, IModel<List<ProjectBaseData>> model) {
        return new ListView<ProjectBaseData>(id, model) {
            @Override
            protected void populateItem(final ListItem<ProjectBaseData> item) {
                item.add(new Label("projectname", model(from(item.getModel()).getName())));
                item.add(createDeleteProjectButton(item));
                item.add(createProjectEditButton(item));
            }

            @Override
            protected ListItem<ProjectBaseData> newItem(int index, IModel<ProjectBaseData> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, ProjectBaseData.class));
            }
        };
    }

    private Component createProjectEditButton(ListItem<ProjectBaseData> item) {
        return new Link("editProjectButton") {
            @Override
            public void onClick() {
                setResponsePage(EditProjectPage.class, createParameters(item.getModelObject().getId()));
            }
        };
    }

    private Component createDeleteProjectButton(ListItem<ProjectBaseData> item) {
        return new Link("deleteProjectButton") {
            @Override
            public void onClick() {
                setResponsePage(new DeleteDialog() {
                    @Override
                    protected void onYes() {
                        projectService.deleteProject(item.getModelObject().getId());
                        setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                    }

                    @Override
                    protected void onNo() {
                        setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                    }

                    @Override
                    protected String confirmationText() {
                        return new StringResourceModel("delete.project.confirmation", BudgeteerAdministrationOverview.this).getString();
                    }
                });
            }
        };
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(BudgeteerAdministrationOverview.class);
    }
}
