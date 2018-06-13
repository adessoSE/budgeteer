package org.wickedsource.budgeteer.web.components.security;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NeedsLogin {}
