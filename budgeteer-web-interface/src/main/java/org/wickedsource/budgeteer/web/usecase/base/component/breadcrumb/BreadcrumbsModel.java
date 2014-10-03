package org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb;

import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.PropertyLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BreadcrumbsModel implements IModel<List<Breadcrumb>> {

    /**
     * Name of the property key for the breadcrumb title of a page.
     */
    private static final String BREADCRUMB_TITLE_KEY = "breadcrumb.title";

    private List<Class<? extends BasePage>> crumbs;

    public BreadcrumbsModel(Class<? extends BasePage>... crumbs) {
        this.crumbs = Arrays.asList(crumbs);
    }

    @Override
    public List<Breadcrumb> getObject() {
        List<Breadcrumb> breadcrumbsList = new ArrayList<Breadcrumb>();
        for (Class<? extends BasePage> crumb : crumbs) {
            breadcrumbsList.add(new Breadcrumb(crumb, getBreadcrumbTitle(crumb)));
        }
        return breadcrumbsList;
    }

    @Override
    public void setObject(List<Breadcrumb> object) {

    }

    @Override
    public void detach() {

    }

    /**
     * Reads the breadcrumb title from the properties file of the given page class.
     */
    private String getBreadcrumbTitle(Class<? extends BasePage> clazz) {
        return PropertyLoader.getProperty(clazz, BREADCRUMB_TITLE_KEY);
    }
}
