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
        if (getModelObject().getMail() == null) {
            mailTextField = new EmailTextField("mail");
        }
        else {
            mailTextField = new EmailTextField("mail", model(from(getModelObject().getMail())));
        }

        TextField<String> usernameTextField = (TextField) new TextField<>("usernameTextField", new Model<>("")).add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                    }
                }
        );
        TextField<String> usernameRetypedTextField = (TextField<String>) new TextField<>("usernameRetypedTextField", new Model<>("")).add(
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
                if(newUsername.isEmpty() || newUsernameRetyped.isEmpty()){
                    feedbackPanelChangeUsername.error("Username may not be empty!");
                    target.add(feedbackPanelChangeUsername);
                    return;
                }
                if(!newUsernameRetyped.equals(newUsername)){
                    feedbackPanelChangeUsername.error("WRONG!");
                }else{
                    EditUserForm.this.getModelObject().setName(newUsername);
                    try {
                        userService.saveUser(EditUserForm.this.getModelObject(), false);
                        feedbackPanelChangeUsername.success("Username successfully changed!");
                        username.setDefaultModel(model(from(EditUserForm.this.getModel()).getName()));
                        target.add(username);
                    } catch (UsernameAlreadyInUseException e) {
                        feedbackPanelChangeUsername.error("Username already in use");
                    } catch (MailAlreadyInUseException e) {
                        feedbackPanelChangeUsername.error("Email already in use");
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


        PasswordTextField currentPasswordTextField = (PasswordTextField) new PasswordTextField("currentPasswordTextField", new Model<>("")).add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {}
                }
        );
        PasswordTextField passwordTextField = (PasswordTextField) new PasswordTextField("passwordTextField", new Model<>("")).add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {}
                }
        );
        PasswordTextField passwordRetypedTextField = (PasswordTextField) new PasswordTextField("passwordRetypedTextField", new Model<>("")).add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {}
                }
        );
        CustomFeedbackPanel feedbackPanelChangePassword = new CustomFeedbackPanel("feedbackChangePassword");
        feedbackPanelChangePassword.setOutputMarkupId(true);

        Form changePassword = new Form("passwordForm");
        AjaxLink submitButton3 = new AjaxLink("submitButton3") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String currentPassowrd = currentPasswordTextField.getModelObject();
                String newPassword = passwordTextField.getModelObject();
                String newPasswordRetyped = passwordRetypedTextField.getModelObject();
                if(currentPassowrd.isEmpty() || newPassword.isEmpty() || newPasswordRetyped.isEmpty()){
                    feedbackPanelChangePassword.error("Password may not be empty!");
                    target.add(feedbackPanelChangePassword);
                    return;
                }

                if(!newPassword.equals(newPasswordRetyped)){//TODO
                    feedbackPanelChangePassword.error("The new password does not blabla bla with blablabla!");
                }else{
                    try {
                        currentPassowrd = new PasswordHasher().hash(currentPassowrd);
                        String currentPassowrdCheck = userService.getUserById(EditUserForm.this.getModelObject().getId()).getPassword();
                        if(!currentPassowrd.equals(currentPassowrdCheck)){
                            feedbackPanelChangePassword.error("The current password is wrong");
                        }else{
                            EditUserForm.this.getModelObject().setPassword(currentPassowrd);
                            try {
                                userService.saveUser(EditUserForm.this.getModelObject(), true);
                            } catch (UsernameAlreadyInUseException e) {
                              //TODO  e.printStackTrace();
                            } catch (MailAlreadyInUseException e) {
                             //TODO   e.printStackTrace();
                            }
                            feedbackPanelChangePassword.success("Password successfully changed!");
                        }
                    } catch (UserIdNotFoundException e) {
                        e.printStackTrace();//TODO
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

        mailTextField.setRequired(true);

        add(username);
        add(mailTextField);
        add(defaultProjectDropdown);
        add(lastLogin);

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
                boolean changePassword = false;

                if (mailTextField.getInput().isEmpty()) {
                    error(getString("form.mail.Required"));
                    return;
                }

                try {
                    EditUserForm.this.getModelObject().setMail(mailTextField.getInput());

                    // If the user has changed his mail address, this will be displayed to him.
                    if (!userService.saveUser(EditUserForm.this.getModelObject(), changePassword)) {
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
        submitButton.setDefaultFormProcessing(true);
        add(submitButton);
    }
}
