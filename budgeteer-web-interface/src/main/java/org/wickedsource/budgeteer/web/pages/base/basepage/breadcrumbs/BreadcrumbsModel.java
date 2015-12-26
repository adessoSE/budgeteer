package org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs;

import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BreadcrumbsModel implements IModel<List<Breadcrumb>> {

    /**
     * Name of the property key for the breadcrumb title of a page.
     */
    private static final String BREADCRUMB_TITLE_KEY = "breadcrumb.title";

    private List<Breadcrumb> crumbsList = new ArrayList<>();

    /**
     * Convenience constructor. Creates breadcrumbs for all page classes passed into the constructor. Uses the property
     * "breadcrumb.title" of the page as the title for the breadcrumbs. If you want to specify the title yourself,
     * use the constructor that takes Breadcrumb objects as parameters.
     *
     * @param crumbs the page classes to create breadcrumbs for.
     */
    @SafeVarargs
    public BreadcrumbsModel(Class<? extends BasePage>... crumbs) {
        for (Class<? extends BasePage> crumb : crumbs) {
            crumbsList.add(new Breadcrumb(crumb, getBreadcrumbTitle(crumb)));
        }
    }

    /**
     * Constructor. Creates a breadcrumb for each breadcrumb object passed into the constructor.
     *
     * @param crumbs the breadcrumbs to create.
     */
    public BreadcrumbsModel(Breadcrumb... crumbs) {
        crumbsList = Arrays.asList(crumbs);
    }

    /**
     * Adds a breadcrumb to this model.
     *
     * @param crumb the breadcrumb to add.
     */
    public void addBreadcrumb(Breadcrumb crumb) {
        crumbsList.add(crumb);
    }

    /**
     * Convenience method for adding a breadcrumb to the page passed as parameter. The title of the breadcrumb will be
     * loaded from the property "breadcrumb.title" of the specifiec page class.
     *
     * @param crumb the page class for which to create a breadcrumb.
     */
    public void addBreadcrumb(Class<? extends BasePage> crumb) {
        crumbsList.add(new Breadcrumb(crumb, getBreadcrumbTitle(crumb)));
    }

    @Override
    public List<Breadcrumb> getObject() {
        return crumbsList;
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
