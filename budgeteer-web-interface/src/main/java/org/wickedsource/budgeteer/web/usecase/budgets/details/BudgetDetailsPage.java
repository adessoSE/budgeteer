package org.wickedsource.budgeteer.web.usecase.budgets.details;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.BudgetBasePage;

public class BudgetDetailsPage extends BudgetBasePage {

    public BudgetDetailsPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return null;
    }

}
