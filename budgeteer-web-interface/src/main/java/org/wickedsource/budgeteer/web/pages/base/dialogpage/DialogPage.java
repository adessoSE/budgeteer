package org.wickedsource.budgeteer.web.pages.base.dialogpage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class DialogPage extends WebPage {

    private Class<? extends WebPage> backlinkPage;

    private PageParameters backlinkParameters;

    public DialogPage(PageParameters parameters, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(parameters);
        this.backlinkPage = backlinkPage;
        this.backlinkParameters = backlinkParameters;
    }

    public DialogPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        this.backlinkPage = backlinkPage;
        this.backlinkParameters = backlinkParameters;
    }

    protected Link createBacklink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                goBack();
            }
        };
    }

    public void goBack(){
       setResponsePage(backlinkPage, backlinkParameters);
    }

}
