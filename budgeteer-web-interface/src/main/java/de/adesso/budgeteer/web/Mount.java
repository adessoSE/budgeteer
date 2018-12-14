package de.adesso.budgeteer.web;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Mount {

    /**
     * The url the annotated page should be mounted to.
     */
    String[] value();

}
