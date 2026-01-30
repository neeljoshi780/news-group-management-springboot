package com.newsgroupmanagement.security;

import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private final UserRepository userRepository;
    private final BruteForceProtection bruteForceProtection;
    public CustomLoginFailureHandler(UserRepository userRepository, BruteForceProtection bruteForceProtection) {
        this.userRepository = userRepository;
        this.bruteForceProtection = bruteForceProtection;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // get email in request
        String email = request.getParameter("email");
        // Store email in session
        HttpSession session = request.getSession(true);
        session.setAttribute("FAILED_EMAIL", email);

        //Check for disabled account
        if (exception instanceof DisabledException) {
            response.sendRedirect("/auth/sign-in?status=disabled");
            return;
        }

        // get user by email
        User user = userRepository.findByEmail(email).orElse(null);

        // Normal logic for invalid/locked
        if(user != null){
            if(user.isAccountNonLocked()){
                bruteForceProtection.increaseFailedAttempts(user);
                response.sendRedirect("/auth/sign-in?status=invalid");
                return;
            }else{
                if(bruteForceProtection.unlockWhenTimeExpired(user)){
                    bruteForceProtection.increaseFailedAttempts(user);
                    response.sendRedirect("/auth/sign-in?status=invalid");
                    return;
                }else{
                    response.sendRedirect("/auth/sign-in?status=locked");
                    return;
                }
            }
        }
        response.sendRedirect("/auth/sign-in?status=notfound");
    }
}
