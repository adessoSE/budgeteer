package de.adesso.budgeteer.web.pages.user.register;

import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.dialogpage.DialogPage;
import de.adesso.budgeteer.web.pages.user.login.LoginPage;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import de.adesso.budgeteer.service.user.MailAlreadyInUseException;
import de.adesso.budgeteer.service.user.UserService;
import de.adesso.budgeteer.service.user.UsernameAlreadyInUseException;
import de.adesso.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wicketstuff.lazymodel.LazyModel;

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
                if (!getModelObject().getPassword().equals(getModelObject().getPasswordConfirmation())) {
                    error(getString("message.wrongPasswordConfirmation"));
                    return;
                }
                try {
                    service.registerUser(getModelObject().getUsername(), getModelObject().getMail(), getModelObject().getPassword());
                    setResponsePage(LoginPage.class, new PageParameters().add("verificationsent", "true"));
                } catch (UsernameAlreadyInUseException e) {
                    this.error(getString("message.duplicateUserName"));
                } catch (MailAlreadyInUseException e) {
                    this.error(getString("message.duplicateMail"));
                }
            }
        };
        add(form);
        form.add(new CustomFeedbackPanel("feedback"));
        form.add(new RequiredTextField<>("username", model(LazyModel.from(form.getModel()).getUsername())));
        form.add(new EmailTextField("mail", model(LazyModel.from(form.getModel()).getMail())).setRequired(true));
        form.add(new PasswordTextField("password", model(LazyModel.from(form.getModel()).getPassword())));
        form.add(new PasswordTextField("passwordConfirmation", model(LazyModel.from(form.getModel()).getPasswordConfirmation())));
        form.add(new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class));
    }
}
