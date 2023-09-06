package org.wickedsource.budgeteer.web.pages.user.edit;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.EditUserData;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.user.edit.edituserform.EditUserForm;

@Mount({"user/edit/${id}"})
public class EditUserPage extends DialogPageWithBacklink {

  @SpringBean private UserService userService;

  public EditUserPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
    super(backlinkPage, backlinkParameters);
    addComponents(backlinkParameters);
  }

  private void addComponents(PageParameters backlinkParameters) {
    EditUserData editUserData =
        userService.loadUserToEdit(Long.parseLong(backlinkParameters.get("userId").toString()));
    Form<EditUserData> form = new EditUserForm("form", Model.of(editUserData));
    add(form);
    add(createBacklink("backlink1"));
    form.add(createBacklink("backlink2"));
  }
}
