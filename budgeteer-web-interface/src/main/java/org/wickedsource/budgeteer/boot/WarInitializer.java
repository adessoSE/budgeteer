package org.wickedsource.budgeteer.boot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * This class is needed for deployment on an application server.
 * It is the counterpart of the main method in WicketWebApplication.
 *
 * @author Stefan Kloe
 */
public class WarInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BudgeteerBooter.class);
    }

}
