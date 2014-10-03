package org.wickedsource.budgeteer.web;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import java.util.List;
import java.util.Locale;

public class PropertyLoader {

    /**
     * Loads the value of the given property key from the resource bundle that belongs to the given Wicket component class.
     *
     * @param propertySource the Wicket component whose property to load
     * @param propertyKey    the name of the property to load
     * @return the value of the property
     */
    public static String getProperty(Class<? extends Component> propertySource, String propertyKey) {
        String resourceValue = null;
        List<IStringResourceLoader> resourceLoaders = Application.get()
                .getResourceSettings()
                .getStringResourceLoaders();
        Locale locale = Session.get().getLocale();
        String style = Session.get().getStyle();

        for (IStringResourceLoader stringResourceLoader : resourceLoaders) {
            resourceValue = stringResourceLoader.loadStringResource(propertySource, propertyKey, locale, style, null);
            if (resourceValue != null) {
                break;
            }
        }

        if (resourceValue == null) {
            throw new RuntimeException(String.format("Property %s could not be found for class %s!", propertyKey, propertySource.getName()));
        } else {
            return resourceValue.toString();
        }
    }
}
