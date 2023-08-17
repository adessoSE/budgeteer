package org.wickedsource.budgeteer.web.pages.user.login;

import static org.apache.wicket.model.LambdaModel.of;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.InvalidLoginCredentialsException;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.user.register.RegisterPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;

@Mount("/login")
public class LoginPage extends DialogPage {

  @SpringBean private UserService userService;

  public LoginPage() {
    build();
  }

  private void build() {
    var form =
        new Form<>("loginForm", Model.of((new LoginCredentials()))) {
          @Override
          protected void onSubmit() {
            try {
              var user =
                  userService.login(getModelObject().getUsername(), getModelObject().getPassword());
              BudgeteerSession.get().login(user);
              var nextPage = new SelectProjectPage(LoginPage.class, getPageParameters());
              setResponsePage(nextPage);
            } catch (InvalidLoginCredentialsException e) {
              error(getString("message.invalidLogin"));
            }
          }
        };
    add(form);

    form.add(new CustomFeedbackPanel("feedback"));
    form.add(
        new RequiredTextField<>(
            "username",
            of(form.getModel(), LoginCredentials::getUsername, LoginCredentials::setUsername)));
    form.add(
        new PasswordTextField(
            "password",
            of(form.getModel(), LoginCredentials::getPassword, LoginCredentials::setPassword)));
    form.add(new BookmarkablePageLink<RegisterPage>("registerLink", RegisterPage.class));
  }
}
