package org.wickedsource.budgeteer.web.pages.user.edit.edituserform;

import static org.apache.wicket.model.LambdaModel.of;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.*;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;

public class EditUserForm extends Form<EditUserData> {

  @SpringBean private UserService userService;

  public EditUserForm(String id) {
    super(
        id,
        new ClassAwareWrappingModel<>(
            Model.of(new EditUserData(BudgeteerSession.get().getLoggedInUser().getId())),
            EditUserData.class));
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

    RequiredTextField<String> usernameRequiredTextField =
        new RequiredTextField<>(
            "username", of(getModel(), EditUserData::getName, EditUserData::setName));
    EmailTextField mailTextField;
    if (getModelObject().getMail() == null) mailTextField = new EmailTextField("mail");
    else
      mailTextField =
          new EmailTextField("mail", of(getModel(), EditUserData::getMail, EditUserData::setMail));
    PasswordTextField currentPasswordTextField = new PasswordTextField("currentPassword");
    PasswordTextField newPasswordTextField = new PasswordTextField("newPassword");
    PasswordTextField newPasswordConfirmationTextField =
        new PasswordTextField("newPasswordConfirmation");

    usernameRequiredTextField.setRequired(true);
    mailTextField.setRequired(true);
    currentPasswordTextField.setRequired(false);
    newPasswordTextField.setRequired(false);
    newPasswordConfirmationTextField.setRequired(false);

    add(usernameRequiredTextField);
    add(mailTextField);
    add(currentPasswordTextField);
    add(newPasswordTextField);
    add(newPasswordConfirmationTextField);

    /*
     * The checks of the input fields must be done manually,
     * because setDefaultFormProcessing cannot be set to true,
     * otherwise the following error will be thrown:
     *
     * Last cause: Attempt to set a model object on a component without a model! Either pass an IModel to the constructor or use #setDefaultModel(new SomeModel(object)). Component: form:actualPassword
     */
    Button submitButton =
        new Button("submitButton") {
          @Override
          public void onSubmit() {
            boolean changePassword = false;

            if (usernameRequiredTextField.getInput().isEmpty()) {
              error(getString("form.username.Required"));
              return;
            }

            if (mailTextField.getInput().isEmpty()) {
              error(getString("form.mail.Required"));
              return;
            }

            if (currentPasswordTextField.getInput().isEmpty()
                && (!newPasswordTextField.getInput().isEmpty()
                    || !newPasswordConfirmationTextField.getInput().isEmpty())) {
              error(getString("form.currentPassword.Required"));
              return;
            }

            if (!currentPasswordTextField.getInput().isEmpty()) {
              if (newPasswordTextField.getInput().isEmpty()) {
                error(getString("form.newPassword.Required"));
                return;
              }

              if (newPasswordConfirmationTextField.getInput().isEmpty()) {
                error(getString("form.newPasswordConfirmation.Required"));
                return;
              }

              if (!userService.checkPassword(
                  EditUserForm.this.getModelObject().getId(),
                  currentPasswordTextField.getInput())) {
                error(getString("message.wrongPassword"));
                return;
              }

              if (!newPasswordTextField
                  .getInput()
                  .equals(newPasswordConfirmationTextField.getInput())) {
                error(getString("message.wrongPasswordConfirmation"));
                return;
              }

              EditUserForm.this.getModelObject().setPassword(newPasswordTextField.getInput());
              changePassword = true;
            }

            try {
              EditUserForm.this.getModelObject().setName(usernameRequiredTextField.getInput());
              EditUserForm.this.getModelObject().setMail(mailTextField.getInput());

              // If the user has changed his mail address, this will be displayed to him.
              if (!userService.saveUser(EditUserForm.this.getModelObject(), changePassword)) {
                userService.createNewVerificationTokenForUser(
                    userService.getUserById(EditUserForm.this.getModelObject().getId()));
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
    submitButton.setDefaultFormProcessing(false);
    add(submitButton);
  }
}
