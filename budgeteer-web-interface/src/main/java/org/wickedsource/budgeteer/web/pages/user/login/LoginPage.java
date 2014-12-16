package org.wickedsource.budgeteer.web.pages.user.login;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.InvalidLoginCredentialsException;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.user.register.RegisterPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/login")
public class LoginPage extends DialogPage {

    @SpringBean
    private UserService userService;

    public LoginPage() {
        Form<LoginCredentials> form = new Form<LoginCredentials>("loginForm", model(from(new LoginCredentials()))) {
            @Override
            protected void onSubmit() {
                try {
                    User user = userService.login(getModelObject().getUsername(), getModelObject().getPassword());
                    BudgeteerSession.get().login(user);
                    SelectProjectPage nextPage = new SelectProjectPage(LoginPage.class, getPageParameters());
                    setResponsePage(nextPage);
                } catch (InvalidLoginCredentialsException e) {
                    error(getString("message.invalidLogin"));
                }
            }
        };
        add(form);

        form.add(new CustomFeedbackPanel("feedback"));
        form.add(new RequiredTextField<String>("username", model(from(form.getModel()).getUsername())));
        form.add(new PasswordTextField("password", model(from(form.getModel()).getPassword())));
        form.add(new BookmarkablePageLink<RegisterPage>("registerLink", RegisterPage.class));
    }
}
