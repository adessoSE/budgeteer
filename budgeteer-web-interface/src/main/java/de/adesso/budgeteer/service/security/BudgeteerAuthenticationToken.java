package de.adesso.budgeteer.service.security;

import de.adesso.budgeteer.service.user.User;
import de.adesso.budgeteer.web.BudgeteerSession;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

/**
 * A {@link AbstractAuthenticationToken} that is used to set the {@link Authentication} object
 * in the {@link SecurityContext}.
 *
 * @see BudgeteerSession#login(User)
 */
public class BudgeteerAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;

    /**
     * Creates a new token with the given name as the principal.
     *
     * @param name
     *          The username of the user.
     */
    public BudgeteerAuthenticationToken(String name) {
        super(null);

        this.principal = name;

        this.setAuthenticated(true);
    }

    /**
     *
     * @return
     *          A placeholder password.
     */
    @Override
    public String getCredentials() {
        return "password";
    }

    /**
     *
     * @return
     *          The username of the current user.
     */
    @Override
    public String getPrincipal() {
        return principal;
    }

}
