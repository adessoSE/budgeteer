package org.wickedsource.budgeteer.web.pages.user.edit.edituserform;

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
import org.wickedsource.budgeteer.web.pages.administration.Project;
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

        EmailTextField mailTextField;
        if (getModelObject().getMail() == null) {
            mailTextField = new EmailTextField("mail");
        }
        else {
            mailTextField = new EmailTextField("mail", model(from(getModelObject().getMail())));
        }

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
        defaultProjectDropdown.setRequired(true);

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
