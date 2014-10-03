package org.wickedsource.budgeteer.web.components;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import java.util.List;
import java.util.Locale;

public class PropertyLoader {

    public static String getProperty(Class<?> propertySource, String propertyKey) {
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
