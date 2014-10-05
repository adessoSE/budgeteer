package org.wickedsource.budgeteer.web;

import org.apache.wicket.IPageFactory;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.session.DefaultPageFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

/**
 * A Page Factory that loads Wicket pages from the spring application context. Wicket pages must be scoped with the
 * "prototype" scope, so that each time a page is requested from the application context, a new instance is returned.
 */
public class SpringPageFactory implements IPageFactory {

    private ApplicationContext applicationContext;

    public SpringPageFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <C extends IRequestablePage> C newPage(Class<C> pageClass) {
        Scope scope = pageClass.getAnnotation(Scope.class);
        if (scope == null) {
            throw new WicketRuntimeException(String.format("Wicket pages need to have the following annotation: @Scope(\"prototype\"). Fix that for class %s", pageClass));
        }
        return applicationContext.getBean(pageClass);
    }

    @Override
    public <C extends IRequestablePage> C newPage(Class<C> pageClass, PageParameters parameters) {
        return applicationContext.getBean(pageClass, parameters);
    }

    @Override
    public <C extends IRequestablePage> boolean isBookmarkable(Class<C> pageClass) {
        return new DefaultPageFactory().isBookmarkable(pageClass);
    }
}
