package de.adesso.budgeteer.rest.security.userdetails;

import de.adesso.budgeteer.core.security.domain.AuthenticationUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsMapper {
    public UserDetails toUserDetails(AuthenticationUser authenticationUser) {
        return new UserDetailsImpl(authenticationUser.getId(), authenticationUser.getUsername(), authenticationUser.getPassword());
    }
}
