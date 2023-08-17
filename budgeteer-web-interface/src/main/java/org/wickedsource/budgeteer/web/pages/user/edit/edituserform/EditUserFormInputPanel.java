package org.wickedsource.budgeteer.web.pages.user.edit.edituserform;

import static org.apache.wicket.model.LambdaModel.of;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.user.EditUserData;

public class EditUserFormInputPanel extends GenericPanel<EditUserData> {
  public EditUserFormInputPanel(String id, IModel<EditUserData> model) {
    super(id, model);
    var usernameRequiredTextField =
        new RequiredTextField<>(
                "username", of(getModel(), EditUserData::getName, EditUserData::setName))
            .setRequired(true);
    var currentPasswordTextField =
        new PasswordTextField(
                "currentPassword",
                of(getModel(), EditUserData::getPassword, EditUserData::setPassword))
            .setRequired(true);
    var newPasswordTextField =
        new PasswordTextField(
                "newPassword",
                of(getModel(), EditUserData::getNewPassword, EditUserData::setNewPassword))
            .setRequired(true);
    var newPasswordConfirmationTextField =
        new PasswordTextField(
                "newPasswordConfirmation",
                of(
                    getModel(),
                    EditUserData::getNewPasswordConfirmation,
                    EditUserData::setNewPasswordConfirmation))
            .setRequired(true);

    var form =
        new Form<>("form", model) {
          @Override
          protected void onValidate() {
            if (currentPasswordTextField.getInput().isEmpty()
                && (!newPasswordTextField.getInput().isEmpty()
                    || !newPasswordConfirmationTextField.getInput().isEmpty())) {
              error(getString("form.currentPassword.Required"));
              return;
            }
            if (currentPasswordTextField.getInput().isEmpty()) {
              return;
            }

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
            }
          }
        };

    usernameRequiredTextField.setRequired(true);
    currentPasswordTextField.setRequired(false);
    newPasswordTextField.setRequired(false);
    newPasswordConfirmationTextField.setRequired(false);

    form.add(usernameRequiredTextField);
    form.add(currentPasswordTextField);
    form.add(newPasswordTextField);
    form.add(newPasswordConfirmationTextField);
    add(form);
  }
}
