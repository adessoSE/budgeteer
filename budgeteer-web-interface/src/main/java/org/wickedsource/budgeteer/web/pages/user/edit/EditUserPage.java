package org.wickedsource.budgeteer.web.pages.user.edit;

import de.adesso.budgeteer.core.user.port.in.GetUserWithEmailUseCase;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.EditUserModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.user.edit.edituserform.EditUserForm;

@Mount({"user/edit/${id}"})
public class EditUserPage extends DialogPageWithBacklink {
  @SpringBean private GetUserWithEmailUseCase getUserWithEmailUseCase;

  public EditUserPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
    super(backlinkPage, backlinkParameters);
    addComponents(backlinkParameters);
  }

  private void addComponents(PageParameters backlinkParameters) {
    var userWithEmail =
        getUserWithEmailUseCase.getUserWithEmail(backlinkParameters.get("userId").toLong());
    var editUserModel =
        new EditUserModel(userWithEmail.getId(), userWithEmail.getName(), userWithEmail.getEmail());
    Form<EditUserModel> form = new EditUserForm("form", () -> editUserModel);
    add(form);
    add(createBacklink("backlink1"));
    form.add(createBacklink("backlink2"));
  }
}
