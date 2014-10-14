package org.wickedsource.budgeteer.web.usecase.budgets.details;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;

public class BudgetDetailsPage extends BasePage {

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return null;
    }

    /**
     * Creates a valid PageParameters object to pass into the constructor of this page class.
     *
     * @param budgetId id of the budget whose details to display.
     * @return a valid PageParameters object.
     */
    public static PageParameters createParameters(long budgetId) {
        PageParameters parameters = new PageParameters();
        parameters.add("id", budgetId);
        return parameters;
    }

    protected long getBudgetId() {
        return getPageParameters().get("id").toLong();
    }

}
