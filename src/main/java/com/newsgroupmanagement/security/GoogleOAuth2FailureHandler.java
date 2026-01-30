package com.newsgroupmanagement.security;

import com.newsgroupmanagement.exception.OAuth2AuthenticationProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleOAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        //Check for disabled account
        if (exception instanceof DisabledException) {
            response.sendRedirect("/auth/sign-in?oauth2Error=account_disabled");
            return;
        }

        if(exception instanceof OAuth2AuthenticationProcessingException){
            response.sendRedirect("/auth/sign-in?oauth2Error=email_sign_in_required");
        }
    }
}
