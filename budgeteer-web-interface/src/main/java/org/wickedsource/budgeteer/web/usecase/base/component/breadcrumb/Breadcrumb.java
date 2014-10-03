package org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb;


import org.wickedsource.budgeteer.web.usecase.base.BasePage;

public class Breadcrumb {

    private Class<? extends BasePage> targetPage;

    private String title;

    public Breadcrumb(Class<? extends BasePage> targetPage, String title) {
        this.title = title;
        this.targetPage = targetPage;
    }

    public Class<?> getTargetPage() {
        return targetPage;
    }

    public String getTitle() {
        return title;
    }

}
