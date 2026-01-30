package com.newsgroupmanagement.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            return super.attemptAuthentication(request, response);
        } catch (AuthenticationException e) {
            if (e.getCause() instanceof DisabledException) {
                // Pass DisabledException directly to the failure handler
                throw new DisabledException(e.getCause().getMessage());
            }
            throw e; // Rethrow other exceptions
        }
    }
}
