package de.adesso.budgeteer.web.pages.user.forgotpassword;

import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.dialogpage.DialogPage;
import de.adesso.budgeteer.web.pages.user.login.LoginPage;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import de.adesso.budgeteer.service.user.MailNotFoundException;
import de.adesso.budgeteer.service.user.MailNotVerifiedException;
import de.adesso.budgeteer.service.user.UserService;
import de.adesso.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wicketstuff.lazymodel.LazyModel;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/forgotpassword")
public class ForgotPasswordPage extends DialogPage {

    @SpringBean
    private UserService service;

    public ForgotPasswordPage() {
        Injector.get().inject(this);
        Form<ForgotPasswordData> form = new Form<ForgotPasswordData>("forgotPasswordForm", model(from(new ForgotPasswordData()))) {
            @Override
            protected void onSubmit() {
                try {
                    service.resetPassword(getModelObject().getMail());
                    success(getString("message.success"));
                } catch (MailNotFoundException e) {
                    error(getString("message.mailNotFound"));
                } catch (MailNotVerifiedException e) {
                    error(getString("message.mailNotVerified"));
                }
            }
        };
        add(form);
        form.add(new CustomFeedbackPanel("feedback"));
        form.add(new EmailTextField("mail", model(LazyModel.from(form.getModel()).getMail())).setRequired(true));
        form.add(new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class));
    }
}
