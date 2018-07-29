package org.wickedsource.budgeteer.service.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.web.components.security.NeedsLogin;
import org.wickedsource.budgeteer.service.user.UserService;

/**
 * A configuration to set up spring boot security. Only used for authorization. Authentication
 * is performed via the {@link NeedsLogin} annotation and the {@link UserService}.
 *
 * @see org.wickedsource.budgeteer.web.BudgeteerSession#login(User)
 * @see UserService#login(String, String)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .frameOptions().sameOrigin() // allow iframes on the same domain (required for editing contracts)
                .and()
                .csrf().disable() // disable CSRF
                .addFilter(new SecurityContextPersistenceFilter()) // add a new filter to retrieve the AuthenticationContext from the http session
                .antMatcher("/**").anonymous(); // @NeedsLogin annotation deals with allowing authenticated access only
    }

}
