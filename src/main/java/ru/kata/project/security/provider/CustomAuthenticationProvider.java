package ru.kata.project.security.provider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.project.security.entity.UserDetailsEntity;
import ru.kata.project.security.service.UserService;
import ru.kata.project.security.utility.error.AccountDeletedException;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    public CustomAuthenticationProvider(UserService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);

        if (userDetails instanceof UserDetailsEntity myUser) {
            if (!myUser.isAccountNonDeleted()) {
                throw new AccountDeletedException("Account is deleted");
            }
        }
    }
}