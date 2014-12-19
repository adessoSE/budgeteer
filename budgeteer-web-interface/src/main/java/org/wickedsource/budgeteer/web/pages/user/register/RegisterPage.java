package org.wickedsource.budgeteer.web.pages.user.register;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.service.user.UsernameAlreadyInUseException;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/register")
public class RegisterPage extends DialogPage {

    @SpringBean
    private UserService service;

    public RegisterPage() {
        Injector.get().inject(this);
        Form<RegistrationData> form = new Form<RegistrationData>("registrationForm", model(from(new RegistrationData()))) {
            @Override
            protected void onSubmit() {
                if(!getModelObject().getPassword().equals(getModelObject().getPasswordConfirmation())){
                    error(getString("message.wrongPasswordConfirmation"));
                    return;
                }
                try {
                    service.registerUser(getModelObject().getUsername(), getModelObject().getPassword());
                    setResponsePage(LoginPage.class);
                } catch(UsernameAlreadyInUseException e){
                    this.error(getString("message.duplicateUserName"));
                }
            }
        };
        add(form);
        form.add(new CustomFeedbackPanel("feedback"));
        form.add(new RequiredTextField<String>("username", model(from(form.getModel()).getUsername())));
        form.add(new PasswordTextField("password", model(from(form.getModel()).getPassword())));
        form.add(new PasswordTextField("passwordConfirmation", model(from(form.getModel()).getPasswordConfirmation())));
        form.add(new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class));
    }
}
