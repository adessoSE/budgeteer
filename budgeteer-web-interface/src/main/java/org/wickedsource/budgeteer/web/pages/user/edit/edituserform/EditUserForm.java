package org.wickedsource.budgeteer.web.pages.user.edit.edituserform;

import de.adesso.budgeteer.core.user.port.in.UpdateUserUseCase;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.EditUserModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;

public class EditUserForm extends Form<EditUserModel> {

  @SpringBean private UpdateUserUseCase updateUserUseCase;

  public EditUserForm(String id, IModel<EditUserModel> model) {
    super(id, model);
    Injector.get().inject(this);
    addComponents();
  }

  private void addComponents() {
    CustomFeedbackPanel feedbackPanel = new CustomFeedbackPanel("feedback");
    add(feedbackPanel);

    var usernameRequiredTextField =
        new RequiredTextField<>(
            "username", LambdaModel.of(getModel(), EditUserModel::getName, EditUserModel::setName));
    var mailTextField =
        new EmailTextField(
            "mail", LambdaModel.of(getModel(), EditUserModel::getMail, EditUserModel::setMail));
    PasswordTextField currentPasswordTextField =
        new PasswordTextField(
            "currentPassword",
            LambdaModel.of(getModel(), EditUserModel::getPassword, EditUserModel::setPassword));
    PasswordTextField newPasswordTextField =
        new PasswordTextField(
            "newPassword",
            LambdaModel.of(
                getModel(), EditUserModel::getNewPassword, EditUserModel::setNewPassword));
    PasswordTextField newPasswordConfirmationTextField =
        new PasswordTextField(
            "newPasswordConfirmation",
            LambdaModel.of(
                getModel(),
                EditUserModel::getNewPasswordConfirmation,
                EditUserModel::setNewPasswordConfirmation));

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

    var submitButton =
        new Button("submitButton") {
          @Override
          public void onSubmit() {
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

              if (!newPasswordTextField
                  .getInput()
                  .equals(newPasswordConfirmationTextField.getInput())) {
                error(getString("message.wrongPasswordConfirmation"));
                return;
              }
            }

            var editUserModel = EditUserForm.this.getModelObject();
            try {
              updateUserUseCase.updateUser(
                  new UpdateUserUseCase.UpdateUserCommand(
                      editUserModel.getId(),
                      editUserModel.getName(),
                      editUserModel.getMail(),
                      editUserModel.getPassword(),
                      editUserModel.getNewPassword()));
            } catch (de.adesso.budgeteer.core.user.UsernameAlreadyInUseException e) {
              error(getString("message.duplicateUserName"));
            } catch (de.adesso.budgeteer.core.user.InvalidLoginCredentialsException e) {
              error(getString("message.wrongPassword"));
            } catch (de.adesso.budgeteer.core.user.MailAlreadyInUseException e) {
              error(getString("message.duplicateMail"));
            }
          }
        };
    add(submitButton);
  }
}
