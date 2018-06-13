package org.wickedsource.budgeteer.web;

import java.util.HashMap;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.user.User;

public class BudgeteerSession extends WebSession {

	// Tags are saved in a Map and correspond to a projectID
	// This makes them persistent when switching projects
	private HashMap<Long, BudgetTagFilter> budgetFilter = new HashMap<>();

	private Double selectedBudgetUnit = 1d;

	private boolean taxEnabled;

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

	/** Returns the ID of the project the user currently browses. */
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
		return budgetFilter.get(getProjectId());
	}

	public void setBudgetFilter(BudgetTagFilter budgetFilter) {
		this.budgetFilter.put(projectId, budgetFilter);
	}

	/**
	 * The unit in which monetary budget values should be displayed. The monetary values shown in the
	 * UI will be divided by this value.
	 *
	 * @return the unit in which to display monetary budget values.
	 */
	public Double getSelectedBudgetUnit() {
		return selectedBudgetUnit;
	}

	public void setSelectedBudgetUnit(Double selectedBudgetUnit) {
		this.selectedBudgetUnit = selectedBudgetUnit;
	}

	public void setTaxEnabled(boolean enabled) {
		this.taxEnabled = enabled;
	}

	public boolean isTaxEnabled() {
		return taxEnabled;
	}
}
