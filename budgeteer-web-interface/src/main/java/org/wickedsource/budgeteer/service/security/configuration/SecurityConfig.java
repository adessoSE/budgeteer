package org.wickedsource.budgeteer.service.security.configuration;

import java.util.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wickedsource.budgeteer.service.security.PasswordHasher;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.components.security.NeedsLogin;

/**
 * A configuration to set up spring boot security. Only used for authorization. Authentication is
 * performed via the {@link NeedsLogin} annotation and the {@link UserService}.
 *
 * @see org.wickedsource.budgeteer.web.BudgeteerSession#login(User)
 * @see UserService#login(String, String)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.headers()
        .frameOptions()
        .sameOrigin() // allow iframes on the same domain (required for editing contracts etc.)
        .and()
        .csrf()
        .disable() // disable CSRF
        .servletApi()
        .disable() // disables the SecurityContextHolderAwareRequestFilter that wraps requests
        .antMatcher("/**")
        .anonymous(); // @NeedsLogin annotation deals with allowing authenticated access only
    // @formatter:on
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    var idForEncode = "bcrypt";
    var encoders = new HashMap<String, PasswordEncoder>();
    encoders.put(idForEncode, new BCryptPasswordEncoder());

    var oldPasswordHasher = new PasswordHasher();
    encoders.put("sha-512", oldPasswordHasher);

    var passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
    passwordEncoder.setDefaultPasswordEncoderForMatches(oldPasswordHasher);

    return passwordEncoder;
  }
}
