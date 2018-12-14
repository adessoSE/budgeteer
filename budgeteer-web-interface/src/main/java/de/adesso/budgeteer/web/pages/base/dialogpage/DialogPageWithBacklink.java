package de.adesso.budgeteer.web.pages.base.dialogpage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import de.adesso.budgeteer.web.components.security.NeedsLogin;

@NeedsLogin
public abstract class DialogPageWithBacklink extends DialogPage {

    private Class<? extends WebPage> backlinkPage;

    private PageParameters backlinkParameters;

    public DialogPageWithBacklink(PageParameters parameters, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(parameters);
        this.backlinkPage = backlinkPage;
        this.backlinkParameters = backlinkParameters;
    }

    public DialogPageWithBacklink(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        this(null, backlinkPage, backlinkParameters);
    }

    protected Link createBacklink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                goBack();
            }
        };
    }

    public void goBack() {
        setResponsePage(backlinkPage, backlinkParameters);
    }
}
