package de.adesso.budgeteer.rest.security.userdetails;

import de.adesso.budgeteer.core.security.port.in.GetAuthenticationUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final GetAuthenticationUserUseCase getAuthenticationUserUseCase;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getAuthenticationUserUseCase.getAuthenticationUser(username)
                .map(userDetailsMapper::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
