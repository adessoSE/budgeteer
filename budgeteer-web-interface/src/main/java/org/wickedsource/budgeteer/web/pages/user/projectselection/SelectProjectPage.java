package org.wickedsource.budgeteer.web.pages.user.projectselection;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;

@Mount("/selectProject")
public class SelectProjectPage extends DialogPageWithBacklink {

    public SelectProjectPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
    }

}
