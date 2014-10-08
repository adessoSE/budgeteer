package org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb;


import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;

import java.io.Serializable;

public class Breadcrumb implements Serializable {

    private Class<? extends BasePage> targetPage;

    private PageParameters parameters;

    private String title;

    public Breadcrumb(Class<? extends BasePage> targetPage, String title) {
        this.title = title;
        this.targetPage = targetPage;
    }

    public Breadcrumb(Class<? extends BasePage> targetPage, PageParameters parameters, String title) {
        this.parameters = parameters;
        this.title = title;
        this.targetPage = targetPage;
    }

    public Class<?> getTargetPage() {
        return targetPage;
    }

    public String getTitle() {
        return title;
    }

    public PageParameters getParameters() {
        return parameters;
    }
}
