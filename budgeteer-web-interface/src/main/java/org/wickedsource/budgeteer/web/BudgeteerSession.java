package org.wickedsource.budgeteer.web;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.user.User;

public class BudgeteerSession extends WebSession {

    private BudgetTagFilter budgetFilter;

    private Double selectedBudgetUnit = 1d;

    private User loggedInUser;

    private long projectId;

    public BudgeteerSession(Request request) {
        super(request);
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean isLoggedIn() {
        return this.loggedInUser != null;
    }

    public void login(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void logout() {
        invalidate();
        replaceSession();
    }

    /**
     * Returns the ID of the project the user currently browses.
     */
    public long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public static BudgeteerSession get() {
        return (BudgeteerSession) WebSession.get();
    }

    public BudgetTagFilter getBudgetFilter() {
        return this.budgetFilter;
    }

    public void setBudgetFilter(BudgetTagFilter budgetFilter) {
        this.budgetFilter = budgetFilter;
    }

    /**
     * The unit in which monetary budget values should be displayed. The monetary values shown in the UI will be divided
     * by this value.
     *
     * @return the unit in which to display monetary budget values.
     */
    public Double getSelectedBudgetUnit() {
        return selectedBudgetUnit;
    }

    public void setSelectedBudgetUnit(Double selectedBudgetUnit) {
        this.selectedBudgetUnit = selectedBudgetUnit;
    }


}
