package org.wickedsource.budgeteer.web.pages.user.edit;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.InvalidLoginCredentialsException;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.service.user.UsernameAlreadyInUseException;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.user.edit.edituserform.EditUserFormInputPanel;

@Mount({"user/edit/${id}"})
public class EditUserPage extends DialogPageWithBacklink {

  @SpringBean private UserService userService;

  public EditUserPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
    super(backlinkPage, backlinkParameters);
    addComponents(backlinkParameters);
  }

  private void addComponents(PageParameters backlinkParameters) {
    var editUserData =
        Model.of(
            userService.loadUserToEdit(
                Long.parseLong(backlinkParameters.get("userId").toString())));
    var form = new Form<>("form");
    add(createBacklink("backlink1"));
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(new EditUserFormInputPanel("editUser", editUserData));
    form.add(createBacklink("backlink2"));
    form.add(
        new Button("submitButton") {
          @Override
          public void onSubmit() {
            try {
              userService.saveUser(editUserData.getObject());
              success(getString("message.success"));
            } catch (UsernameAlreadyInUseException e) {
              error(getString("message.duplicateUserName"));
            } catch (InvalidLoginCredentialsException e) {
              error(getString("message.wrongPassword"));
            }
          }
        });
    add(form);
  }
}
