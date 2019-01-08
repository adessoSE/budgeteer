package org.wickedsource.budgeteer.web.pages.user.edit.edituserform;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.*;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditUserForm extends Form<EditUserData> {

    @SpringBean
    private UserService userService;

    @SpringBean
    private ProjectService projectService;

    private String currentPassword;
    private String newPassword;
    private String newPasswordRetyped;
    private String currentEmail;

    public EditUserForm(String id) {
        super(id, new ClassAwareWrappingModel<>(Model.of(new EditUserData(BudgeteerSession.get().getLoggedInUser().getId())), EditUserData.class));
        addComponents();
    }

    public EditUserForm(String id, IModel<EditUserData> model) {
        super(id, model);
        Injector.get().inject(this);
        addComponents();
    }

    private void addComponents() {
        CustomFeedbackPanel feedbackPanel = new CustomFeedbackPanel("feedback");
        add(feedbackPanel);

        Label lastLogin = new Label("lastLogin", getModelObject().getLastLogin());
        Label username = new Label("username", getModelObject().getName());
        username.setOutputMarkupId(true);

        EmailTextField mailTextField;
        currentEmail = getModelObject().getMail();
        if (currentEmail == null) {
            mailTextField = new EmailTextField("mail");
        }
        else {
            mailTextField = new EmailTextField("mail", model(from(getModelObject()).getMail()));
        }

        mailTextField.setRequired(true);

        add(username);
        add(mailTextField);
        add(lastLogin);

        addChangeUsernameForm(username);
        addChangePasswordForm();
        addDropDownChoice();

        /*
         * The checks of the input fields must be done manually,
         * because setDefaultFormProcessing cannot be set to true,
         * otherwise the following error will be thrown:
         *
         * Last cause: Attempt to set a model object on a component without a model! Either pass an IModel to the constructor or use #setDefaultModel(new SomeModel(object)). Component: form:actualPassword
         */
        Button submitButton = new Button("submitButton") {
            @Override
            public void onSubmit() {
                if (mailTextField.getInput().isEmpty()) {
                    error(EditUserForm.this.getString("form.mail.Required"));
                    return;
                }

                try {
                    EditUserForm.this.getModelObject().setMail(mailTextField.getInput());

                    // If the user has changed his mail address, this will be displayed to him.
                    userService.saveUser(EditUserForm.this.getModelObject(), false);
                    if (!mailTextField.getInput().equals(currentEmail)) {
                        userService.createNewVerificationTokenForUser(userService.getUserById(EditUserForm.this.getModelObject().getId()));
                        success(getString("message.successVerification"));
                    } else {
                        success(getString("message.success"));
                    }
                } catch (UsernameAlreadyInUseException e) {
                    error(getString("message.duplicateUserName"));
                } catch (MailAlreadyInUseException e) {
                    error(getString("message.duplicateMail"));
                } catch (UserIdNotFoundException e) {
                    error(getString("message.error"));
                }
            }
        };
        add(submitButton);
        setDefaultButton(submitButton);
    }

    private void addDropDownChoice() {
        DropDownChoice<ProjectBaseData> defaultProjectDropdown = new DropDownChoice<ProjectBaseData>("defaultProjectDropdown",
                model(from(getModel()).getDefaultProject()),
                projectService.getProjectsForUser(getModelObject().getId()),
                new AbstractChoiceRenderer<ProjectBaseData>(){
                    @Override
                    public Object getDisplayValue(ProjectBaseData object) {
                        return object.getName();
                    }
                }){
            @Override
            protected String getNullValidDisplayValue() {
                return "No default project";
            }
        };
        defaultProjectDropdown.setNullValid(true);
        add(defaultProjectDropdown);
    }

    private void addChangeUsernameForm(Label username) {
        TextField<String> usernameTextField = new TextField<>("usernameTextField", new Model<>(""));
        usernameTextField.add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                    }
                }
        );
        TextField<String> usernameRetypedTextField = new TextField<>("usernameRetypedTextField", new Model<>(""));
        usernameRetypedTextField.add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {}
                }
        );
        CustomFeedbackPanel feedbackPanelChangeUsername = new CustomFeedbackPanel("feedbackChangeUsername");
        feedbackPanelChangeUsername.setOutputMarkupId(true);

        Form changeUsername = new Form("usernameForm");
        AjaxLink submitButton2 = new AjaxLink("submitButton2") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String newUsername = usernameTextField.getModelObject();
                String newUsernameRetyped = usernameRetypedTextField.getModelObject();
                if(newUsername == null || newUsernameRetyped == null || newUsername.isEmpty() || newUsernameRetyped.isEmpty()){
                    feedbackPanelChangeUsername.error(getString("form.username.Required"));
                    target.add(feedbackPanelChangeUsername);
                    return;
                }
                if(!newUsernameRetyped.equals(newUsername)){
                    feedbackPanelChangeUsername.error(getString("message.wrongUsernameConfirmation"));
                }else{
                    EditUserForm.this.getModelObject().setName(newUsername);
                    try {
                        userService.saveUser(EditUserForm.this.getModelObject(), false);
                        feedbackPanelChangeUsername.success(getString("message.success"));
                        username.setDefaultModel(model(from(EditUserForm.this.getModel()).getName()));
                        target.add(username);
                    } catch (UsernameAlreadyInUseException e) {
                        feedbackPanelChangeUsername.error(getString("message.duplicateUserName"));
                    } catch (MailAlreadyInUseException e) {
                        feedbackPanelChangeUsername.error(getString("message.duplicateMail"));
                    }
                }
                target.add(feedbackPanelChangeUsername);
            }
        };
        changeUsername.add(feedbackPanelChangeUsername);
        changeUsername.add(usernameTextField);
        changeUsername.add(usernameRetypedTextField);

        changeUsername.add(submitButton2);
        add(changeUsername);
        add(new AjaxLink("changeUsernameButton") {

            @Override //Clear the feedback panel and the text fields when opening the modal window
            public void onClick(AjaxRequestTarget target) {
                feedbackPanelChangeUsername.clear();
                usernameTextField.setModelObject("");
                usernameRetypedTextField.setModelObject("");
                target.add(feedbackPanelChangeUsername, usernameTextField, usernameRetypedTextField);
            }
        });
    }

    private void addChangePasswordForm() {
        PasswordTextField currentPasswordTextField = (PasswordTextField) new PasswordTextField("currentPasswordTextField", new Model<>("")).add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        currentPassword = ((PasswordTextField) this.getComponent()).getConvertedInput();
                    }
                }
        );
        currentPasswordTextField.setRequired(false);
        PasswordTextField passwordTextField = (PasswordTextField) new PasswordTextField("passwordTextField", new Model<>("")).add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        newPassword = ((PasswordTextField) this.getComponent()).getConvertedInput();
                    }
                }
        );
        passwordTextField.setRequired(false);
        PasswordTextField passwordRetypedTextField = (PasswordTextField) new PasswordTextField("passwordRetypedTextField",  new Model<>("")).add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        newPasswordRetyped = ((PasswordTextField) this.getComponent()).getConvertedInput();
                    }
                }
        );
        passwordRetypedTextField.setRequired(false);
        CustomFeedbackPanel feedbackPanelChangePassword = new CustomFeedbackPanel("feedbackChangePassword");
        feedbackPanelChangePassword.setOutputMarkupId(true);

        Form changePassword = new Form("passwordForm");
        AjaxLink submitButton3 = new AjaxLink("submitButton3") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (currentPassword == null || currentPassword.isEmpty()) {
                    feedbackPanelChangePassword.error(getString("form.currentPassword.Required"));
                    target.add(feedbackPanelChangePassword);
                    return;
                }
                else if (newPassword == null || newPassword.isEmpty()) {
                    feedbackPanelChangePassword.error(getString("form.newPassword.Required"));
                    target.add(feedbackPanelChangePassword);
                    return;
                }
                else if (newPasswordRetyped == null || newPasswordRetyped.isEmpty()) {
                    feedbackPanelChangePassword.error(getString("form.newPasswordConfirmation.Required"));
                    target.add(feedbackPanelChangePassword);
                    return;
                }

                if(!newPassword.equals(newPasswordRetyped)){
                    feedbackPanelChangePassword.error(getString("message.wrongPasswordConfirmation"));
                }else{
                    try {
                        currentPassword = new PasswordHasher().hash(currentPassword);
                        String currentPasswordCheck = userService.getUserById(EditUserForm.this.getModelObject().getId()).getPassword();
                        if(!currentPassword.equals(currentPasswordCheck)){
                            feedbackPanelChangePassword.error(getString("message.wrongPassword"));
                        }else{
                            EditUserForm.this.getModelObject().setPassword(new PasswordHasher().hash(newPassword));
                            try {
                                userService.saveUser(EditUserForm.this.getModelObject(), false);
                            } catch (UsernameAlreadyInUseException e) {
                                feedbackPanelChangePassword.error("message.duplicateUserName");
                            } catch (MailAlreadyInUseException e) {
                                feedbackPanelChangePassword.error("message.duplicateMail");
                            }
                            feedbackPanelChangePassword.success(getString("message.success"));
                        }
                    } catch (UserIdNotFoundException e) {
                        feedbackPanelChangePassword.error("This should not happen.");
                    }
                }
                target.add(feedbackPanelChangePassword);
            }
        };
        changePassword.add(feedbackPanelChangePassword);
        changePassword.add(passwordTextField);
        changePassword.add(passwordRetypedTextField);
        changePassword.add(currentPasswordTextField);
        changePassword.add(submitButton3);
        add(changePassword);
        add(new AjaxLink("changePasswordButton") {

            @Override //Clear the feedback panel and the text fields when opening the modal window
            public void onClick(AjaxRequestTarget target) {
                feedbackPanelChangePassword.clear();
                currentPasswordTextField.setModelObject("");
                passwordTextField.setModelObject("");
                passwordRetypedTextField.setModelObject("");
                target.add(feedbackPanelChangePassword, currentPasswordTextField, passwordTextField, passwordRetypedTextField);
            }
        });
    }
}
