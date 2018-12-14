package de.adesso.budgeteer.web.pages.user.resettoken;

import de.adesso.budgeteer.persistence.user.UserEntity;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import de.adesso.budgeteer.service.user.MailNotFoundException;
import de.adesso.budgeteer.service.user.TokenStatus;
import de.adesso.budgeteer.service.user.UserIdNotFoundException;
import de.adesso.budgeteer.service.user.UserService;
import de.adesso.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import org.wicketstuff.lazymodel.LazyModel;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("resettoken")
public class ResetTokenPage extends DialogPage {

    @SpringBean
    private UserService service;

    public ResetTokenPage() {
        addComponents(new PageParameters());
    }

    public ResetTokenPage(PageParameters pageParameters) {
        handleStatusCode(pageParameters);
        addComponents(pageParameters);
    }

    private void addComponents(PageParameters backlinkParameters) {
        Injector.get().inject(this);
        ResetTokenData resetTokenData = new ResetTokenData();
        try {
            resetTokenData.setMail(service.getUserById(Long.parseLong(backlinkParameters.get("userId").toString())).getMail());
        } catch (UserIdNotFoundException e) {
            e.printStackTrace();
        }
        Form<ResetTokenData> form = new Form<ResetTokenData>("resetTokenForm", model(from(resetTokenData))) {
            @Override
            protected void onSubmit() {
                try {
                    UserEntity userEntity = service.getUserByMail(getModelObject().getMail());

                    if (!userEntity.getMailVerified()) {
                        service.createNewVerificationTokenForUser(userEntity);
                        success(getString("message.mailSent"));
                    } else {
                        error(getString("message.alreadyEnabled"));
                    }
                } catch (MailNotFoundException e) {
                    error(getString("message.mailNotFound"));
                }
            }
        };
        add(form);
        form.add(new CustomFeedbackPanel("feedback"));
        form.add(new EmailTextField("mail", model(LazyModel.from(form.getModel()).getMail())).setRequired(true));
        form.add(new Button("submitButton"));
        form.add(new BookmarkablePageLink("backlink", DashboardPage.class));
    }

    private void handleStatusCode(PageParameters pageParameters) {
        if (!pageParameters.get("valid").isNull() && !pageParameters.get("valid").isEmpty()) {
            int result = pageParameters.get("valid").toInt();

            if (result == TokenStatus.INVALID.statusCode()) {
                error(getString("message.tokenInvalid"));
            } else if (result == TokenStatus.EXPIRED.statusCode()) {
                error(getString("message.tokenExpired"));
            }
        }
    }
}