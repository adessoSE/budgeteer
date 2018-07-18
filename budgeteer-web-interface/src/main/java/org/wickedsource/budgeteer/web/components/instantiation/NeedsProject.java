package org.wickedsource.budgeteer.web.components.instantiation;

import java.lang.annotation.*;

/**
 * Runtime annotation to mark a {@link org.apache.wicket.markup.html.WebPage} that depends on the existence
 * of a selected project.
 *
 * @see BudgeteerRequiresProjectListener
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NeedsProject {

}
