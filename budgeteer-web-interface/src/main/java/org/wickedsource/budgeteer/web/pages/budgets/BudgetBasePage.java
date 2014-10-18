package org.wickedsource.budgeteer.web.pages.budgets;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;

public abstract class BudgetBasePage extends BasePage {

    public BudgetBasePage(PageParameters parameters){
        super(parameters);
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
