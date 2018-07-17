package org.wickedsource.budgeteer.web.components.initialization;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;

/**
 * {@link IComponentInstantiationListener} implementation to validate the presence of
 * a project if components, which are annotated with {@link NeedsProject},
 * get instantiated.
 *
 * @see NeedsProject
 * @see BudgeteerSession#isProjectSelected()
 */
public class BudgeteerRequiresProjectInstantiationListener implements IComponentInstantiationListener {

    @Override
    public void onInstantiation(Component component) {
        boolean requiresProject = component != null && component.getClass().isAnnotationPresent(NeedsProject.class);

        if(requiresProject) {
            if(!BudgeteerSession.get().isProjectSelected()) {
                throw new RestartResponseAtInterceptPageException(SelectProjectPage.class);
            }
        }
    }

}
