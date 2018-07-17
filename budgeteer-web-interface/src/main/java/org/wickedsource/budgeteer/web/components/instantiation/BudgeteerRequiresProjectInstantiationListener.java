package org.wickedsource.budgeteer.web.components.instantiation;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.BudgeteerSettings;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectWithKeycloakPage;

/**
 * {@link IComponentInstantiationListener} implementation to validate the presence of
 * a project if components, which are annotated with {@link NeedsProject},
 * get instantiated.
 *
 * @see NeedsProject
 * @see BudgeteerSession#isProjectSelected()
 */
public class BudgeteerRequiresProjectInstantiationListener implements IComponentInstantiationListener {

    @SpringBean
    private BudgeteerSettings settings;

    public BudgeteerRequiresProjectInstantiationListener() {
        Injector.get().inject(this);
    }

    @Override
    public void onInstantiation(Component component) {
        boolean requiresProject = component != null && component.getClass().isAnnotationPresent(NeedsProject.class);

        if(requiresProject) {
            if(!BudgeteerSession.get().isProjectSelected()) {
                if(settings.isKeycloakActivated()) {
                    throw new RestartResponseAtInterceptPageException(SelectProjectWithKeycloakPage.class);
                } else {
                    throw new RestartResponseAtInterceptPageException(SelectProjectPage.class);
                }
            }
        }
    }

}
