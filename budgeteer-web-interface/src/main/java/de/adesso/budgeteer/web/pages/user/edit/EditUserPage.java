package de.adesso.budgeteer.web.pages.user.edit;

import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import de.adesso.budgeteer.service.user.EditUserData;
import de.adesso.budgeteer.service.user.UserService;
import de.adesso.budgeteer.web.pages.user.edit.edituserform.EditUserForm;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("user/edit")
public class EditUserPage extends DialogPageWithBacklink {

    @SpringBean
    private UserService userService;

    public EditUserPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        addComponents(backlinkParameters);
    }

    private void addComponents(PageParameters backlinkParameters) {
        EditUserData editUserData = userService.loadUserToEdit(Long.parseLong(backlinkParameters.get("userId").toString()));
        Form<EditUserData> form = new EditUserForm("form", model(from(editUserData)));
        add(form);
        add(createBacklink("backlink1"));
        form.add(createBacklink("backlink2"));
    }
}
