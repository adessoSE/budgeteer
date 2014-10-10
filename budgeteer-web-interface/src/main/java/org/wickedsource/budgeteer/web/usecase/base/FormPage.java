package org.wickedsource.budgeteer.web.usecase.base;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class FormPage extends WebPage {

    public FormPage(PageParameters parameters){
        super(parameters);
    }

    public FormPage() {

    }

}
