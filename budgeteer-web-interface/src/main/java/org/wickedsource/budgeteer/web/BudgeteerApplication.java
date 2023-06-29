package org.wickedsource.budgeteer.web;

import java.util.Set;
import java.util.function.Supplier;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.web.components.instantiation.BudgeteerRequiresProjectListener;
import org.wickedsource.budgeteer.web.components.security.BudgeteerAuthorizationStrategy;
import org.wickedsource.budgeteer.web.components.security.BudgeteerUnauthorizedComponentInstantiationListener;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Component
public class BudgeteerApplication extends WebApplication implements ApplicationContextAware {

  private ApplicationContext context;

  @Autowired private BudgeteerSettings settings;

  @Override
  public Class<? extends WebPage> getHomePage() {
    return DashboardPage.class;
  }

  @Override
  public void init() {
    super.init();

    getCspSettings().blocking().disabled();

    getMarkupSettings().setStripWicketTags(true);
    getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
    getJavaScriptLibrarySettings().setJQueryReference(BudgeteerReferences.getJQueryReference());
    mountPages();

    getSecuritySettings().setAuthorizationStrategy(new BudgeteerAuthorizationStrategy());
    getSecuritySettings()
        .setUnauthorizedComponentInstantiationListener(
            new BudgeteerUnauthorizedComponentInstantiationListener());
    getHeaderResponseDecorators()
        .add(
            response ->
                new JavaScriptFilteredIntoFooterHeaderResponse(response, "JavaScriptContainer"));

    // add component instantiation/onBeforeRender listener
    final BudgeteerRequiresProjectListener listener = new BudgeteerRequiresProjectListener();
    getComponentInstantiationListeners().add(listener);
    getComponentPreOnBeforeRenderListeners().add(listener);
  }

  @Override
  public RuntimeConfigurationType getConfigurationType() {
    String configuration = settings.getConfigurationType();
    if (RuntimeConfigurationType.DEVELOPMENT.name().equalsIgnoreCase(configuration)) {
      return RuntimeConfigurationType.DEVELOPMENT;
    } else {
      return RuntimeConfigurationType.DEPLOYMENT;
    }
  }

  /** Scans the classpath for all pages annotated with the Mount annotation and mounts them. */
  @SuppressWarnings("unchecked")
  private void mountPages() {
    Reflections reflections =
        new Reflections(
            new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("org.wickedsource.budgeteer"))
                .setScanners(new TypeAnnotationsScanner()));
    Set<Class<?>> pagesToMount = reflections.getTypesAnnotatedWith(Mount.class, true);

    for (Class<?> page : pagesToMount) {
      Class<? extends WebPage> pageClass = (Class<? extends WebPage>) page;
      Mount mount = pageClass.getAnnotation(Mount.class);
      for (String mountUrl : mount.value()) {
        mountPage(mountUrl, pageClass);
      }
    }
  }

  @Override
  public Session newSession(Request request, Response response) {
    return new BudgeteerSession(request);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.context = applicationContext;
  }

  @Override
  public Supplier<IExceptionMapper> getExceptionMapperProvider() {
    return BudgeteerExceptionMapper::new;
  }
}
