package org.wickedsource.budgeteer.web.components.breadcrumb;

import org.wickedsource.budgeteer.web.base.BasePage;

import java.util.ArrayList;
import java.util.List;

public class Breadcrumb {

    private Class<? extends BasePage> targetPage;

    private String title;

    public Breadcrumb(Class<? extends BasePage> targetPage, String title) {
        this.title = title;
        this.targetPage = targetPage;
    }

    public Class<? extends BasePage> getTargetPage() {
        return targetPage;
    }

    public String getTitle() {
        return title;
    }

}
