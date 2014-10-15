package org.wickedsource.budgeteer.web;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

public class BudgeteerSession extends WebSession {

    private BudgetTagFilter budgetFilter;

    public BudgeteerSession(Request request) {
        super(request);
    }

    public long getLoggedInUserId() {
        return 1l;
    }

    public static BudgeteerSession get() {
        return (BudgeteerSession) WebSession.get();
    }

    public BudgetTagFilter getBudgetFilter(){
        return this.budgetFilter;
    }

    public void setBudgetFilter(BudgetTagFilter budgetFilter) {
        this.budgetFilter = budgetFilter;
    }
}
