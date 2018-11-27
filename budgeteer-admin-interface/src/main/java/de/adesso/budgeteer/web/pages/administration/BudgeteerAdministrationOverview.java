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
                Component makeAdminList = createRolesList(item);
                Component setPasswordTextField = createPasswordTextField(item);
                Label rolesDropdownLabel = new Label("rolesDropdownLabel");
                // a user may not delete herself/himself unless another admin is present
                if (item.getModelObject().getId() == thisUser.getId()){
                    List<User> allUsers = userService.getAllUsers();
                    deleteUserButton.setVisible(false);
                    makeAdminList.setVisible(false);
                    rolesDropdownLabel.setVisible(false);
                    for(User e : allUsers){
                        if(e.getId() != thisUser.getId() && e.getGlobalRole().equals(UserRole.ADMIN)){
                            deleteUserButton.setVisible(true);
                            makeAdminList.setVisible(true);
                            rolesDropdownLabel.setVisible(true);
                            break;
                        }
                    }
                }
                item.add(deleteUserButton);
                item.add(makeAdminList);
                item.add(setPasswordTextField);
                item.add(createEmailTextField(item));
                item.add(createUsernameTextField(item));
                item.add(rolesDropdownLabel);
            }

            @Override
            protected ListItem<User> newItem(int index, IModel<User> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, User.class));
            }
        };
    }

    private Component createUsernameTextField(ListItem<User> item) {
        TextField<String> textField = new TextField<>("setUsernameTextBox", Model.of(""));
        Form passwordResetField = new Form("usernameResetField"){
            @Override
            protected void onSubmit() {
                if(textField.getModelObject() == null){
                    feedbackPanel.error(getString("username.empty"));
                    return;
                }
                try {
                    userService.setUserUsername(item.getModelObject().getId(), textField.getModelObject());
                    item.getModelObject().setName(textField.getModelObject());
                }catch (UsernameAlreadyInUseException e){
                    feedbackPanel.error(getString("username.in.use"));
                    return;
                }
                feedbackPanel.success(getString("username.success"));
            }
        };
        Button submitButton = new Button("resetUsernameButton");
        return passwordResetField.add(textField, submitButton);
    }

    private Component createPasswordTextField(ListItem<User> item) {
        PasswordTextField textField = new PasswordTextField("setPasswordTextBox", Model.of(""));
        Form passwordResetField = new Form("passwordResetField"){
            @Override
            protected void onSubmit() {
                userService.setUserPassword(item.getModelObject().getId(), textField.getModelObject());
                feedbackPanel.success(getString("password.success"));
            }
        };
        Button submitButton = new Button("resetPasswordButton");
        return passwordResetField.add(textField, submitButton);
    }

    private Component createEmailTextField(ListItem<User> item) {
        EmailTextField textField = new EmailTextField("setEmailTextBox", Model.of(item.getModelObject().getMail()));
        Form emailResetField = new Form("emailResetField"){

            @Override
            protected void onSubmit() {
                try {
                    userService.setUserEmail(item.getModelObject().getId(), textField.getModelObject());
                    item.getModelObject().setMail(textField.getModelObject());
                    feedbackPanel.success(getString("email.success"));
                }catch (MailAlreadyInUseException e){
                    feedbackPanel.error(getString("email.error"));
                }
            }
        };
        Button submitButton = new Button("resetEmailButton");
        return emailResetField.add(textField, submitButton);
    }

    private Link createDeleteUserButton(ListItem<User> item){
        return new Link("deleteUserButton") {
            @Override
            public void onClick() {
                setResponsePage(new org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog() {
                    @Override
                    protected void onYes() {
                        if(thisUser.getId() == item.getModelObject().getId()){
                            userService.removeUser(item.getModelObject().getId());
                            BudgeteerSession.get().logout(); // Log the user out if he deletes himself
                        }else{
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
                        if(thisUser.getId() == item.getModelObject().getId()){
                            return new StringResourceModel("remove.self.confirmation", BudgeteerAdministrationOverview.this).getString();
                        }
                        else{
                            return new StringResourceModel("remove.confirmation", BudgeteerAdministrationOverview.this).getString();
                        }
                    }
                });
            }
        };
    }

    private WebMarkupContainer createRolesList(ListItem<User> item) {
        CheckBox userCheckBox = new CheckBox("userCheckBox", model(from(item.getModelObject().getGlobalRole() == UserRole.USER)));
        userCheckBox.add(new AjaxEventBehavior("ifClicked") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                if(item.getModelObject().getId() == BudgeteerSession.get().getLoggedInUser().getId()){
                    setResponsePage(new DeleteDialog(){

                        @Override
                        protected void onYes() {
                            item.getModelObject().setGlobalRole(UserRole.USER);
                            userService.setGlobalRoleForUser(item.getModelObject().getId(), UserRole.USER);
                            if(item.getModelObject().getId() == thisUser.getId()){
                                BudgeteerSession.get().setLoggedInUser(item.getModelObject());
                            }
                            setResponsePage(LoginPage.class);
                        }

                        @Override
                        protected void onNo() {
                            setResponsePage(BudgeteerAdministrationOverview.class);
                        }

                        @Override
                        protected String confirmationText() {
                            return new StringResourceModel("lose.adminship", BudgeteerAdministrationOverview.this).getString();
                        }
                    });
                }else {
                    userService.setGlobalRoleForUser(item.getModelObject().getId(), UserRole.USER);
                    item.getModelObject().setGlobalRole(UserRole.USER);
                }
                target.add(BudgeteerAdministrationOverview.this);
            }
        });

        CheckBox adminCheckBox = new CheckBox("adminCheckBox", model(from(item.getModelObject().getGlobalRole() == UserRole.ADMIN)));

        adminCheckBox.add(new AjaxEventBehavior("ifClicked") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                userService.setGlobalRoleForUser(item.getModelObject().getId(), UserRole.ADMIN);
                item.getModelObject().setGlobalRole(UserRole.ADMIN);
                userCheckBox.setModel(model(from(Boolean.FALSE)));
                target.add(BudgeteerAdministrationOverview.this);
            }
        });

        WebMarkupContainer checkboxes = new WebMarkupContainer("checkBoxContainer");
        checkboxes.add(userCheckBox.setOutputMarkupId(true));
        checkboxes.add(adminCheckBox);
        return checkboxes;
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

    private Component createProjectEditButton(ListItem<ProjectBaseData> item){
        return new Link("editProjectButton") {
            @Override
            public void onClick() {
                setResponsePage(EditProjectPage.class, createParameters(item.getModelObject().getId()));
            }
        };
    }

    private Component createDeleteProjectButton(ListItem<ProjectBaseData> item){
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
