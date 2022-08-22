package org.wickedsource.budgeteer.web;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.security.BudgeteerAuthenticationToken;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.web.pages.templates.TemplateFilter;

public class BudgeteerSession extends WebSession {

  // Tags are saved in a Map and correspond to a projectID
  // This makes them persistent when switching projects
  private HashMap<Long, BudgetTagFilter> budgetFilter = new HashMap<>();

  private HashMap<Long, TemplateFilter> templateFilter = new HashMap<>();

  private Long remainingBudgetFilterValue = 0L;

  private Double selectedBudgetUnit = 1d;

  private boolean taxEnabled;

  private User loggedInUser;

  private boolean projectSelected;

  private long projectId;

  private transient HttpSession httpSession;

  public BudgeteerSession(Request request) {
    super(request);

    this.httpSession = ((HttpServletRequest) request.getContainerRequest()).getSession();
  }

  public User getLoggedInUser() {
    return loggedInUser;
  }

  public boolean isLoggedIn() {
    return this.loggedInUser != null;
  }

  public void login(User loggedInUser) {
    this.loggedInUser = loggedInUser;

    this.setAuthInSecurityContext(new BudgeteerAuthenticationToken(loggedInUser.getName()));
  }

  public void logout() {
    invalidate();
  }

  /** Returns the ID of the project the user currently browses. */
  public long getProjectId() {
    return this.projectId;
  }

  public void setProjectId(long projectId) {
    this.projectId = projectId;
    this.setProjectSelected(true);
  }

  public boolean isProjectSelected() {
    return projectSelected;
  }

  public void setProjectSelected(boolean projectSelected) {
    this.projectSelected = projectSelected;
  }

  public static BudgeteerSession get() {
    return (BudgeteerSession) WebSession.get();
  }

  public BudgetTagFilter getBudgetFilter() {
    return budgetFilter.get(getProjectId());
  }

  public void setBudgetFilter(BudgetTagFilter budgetTagFilter) {
    this.budgetFilter.put(projectId, budgetTagFilter);
  }

  public TemplateFilter getTemplateFilter() {
    if (templateFilter.get(getProjectId()) == null) {
      return new TemplateFilter(getProjectId());
    } else {
      return templateFilter.get(getProjectId());
    }
  }

  public void setTemplateFilter(TemplateFilter templateFilter) {
    this.templateFilter.put(projectId, templateFilter);
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

  public Long getRemainingBudgetFilterValue() {
    return remainingBudgetFilterValue;
  }

  public void setRemainingBudetFilterValue(Long budgetRemainingFilter) {
    this.remainingBudgetFilterValue = budgetRemainingFilter;
  }

  /**
   * Sets the given {@link Authentication} in the current {@link SecurityContext} and saves the
   * context in the session.
   *
   * @param authToken The {@link Authentication} to set in the {@link SecurityContext}.
   */
  private void setAuthInSecurityContext(Authentication authToken) {
    // save the security context in the session to be able to get it later
    SecurityContextHolder.getContext().setAuthentication(authToken);
    httpSession.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        SecurityContextHolder.getContext());
  }
}
