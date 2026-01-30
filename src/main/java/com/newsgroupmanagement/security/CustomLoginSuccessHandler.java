package com.newsgroupmanagement.security;

import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.model.Role;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.UserRepository;
import com.newsgroupmanagement.service.EmailService;
import com.newsgroupmanagement.util.OtpGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BruteForceProtection bruteForceProtection;
    private final LocaleResolver localeResolver;
    @Autowired
    public CustomLoginSuccessHandler(UserRepository userRepository, EmailService emailService, BruteForceProtection bruteForceProtection, LocaleResolver localeResolver) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bruteForceProtection = bruteForceProtection;
        this.localeResolver = localeResolver;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByEmail(username);

        if(optionalUser.isEmpty()){
            response.sendRedirect("/auth/sign-in?status=notfound");
            return;
        }

        User user = optionalUser.get();

        if(user.isDeleted()){
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            HttpSession session = request.getSession(true);
            session.setAttribute("FAILED_EMAIL", username);
            response.sendRedirect("/auth/sign-in?status=deleted");
            return;
        }

        if(user.isAccountNonLocked()) {
            if (user.isTwoFactorEnabled()) {

                Boolean OTP_VERIFIED = (Boolean) request.getSession().getAttribute("OTP_VERIFIED");

                if (OTP_VERIFIED == null) {

                    // Prevent Spring Security from completing login
                    SecurityContextHolder.clearContext();
                    request.getSession().invalidate();

                    // Store email in session
                    HttpSession session = request.getSession();
                    session.setAttribute("PENDING_USER", username);

                    // Send OTP
                    String twoFactorOtp = OtpGenerator.generateOtp();
                    session.setAttribute("TWO_FACTOR_OTP", twoFactorOtp);
                    emailService.sendTwoFactorOtpEmail(username, twoFactorOtp);

                    response.sendRedirect("/auth/sign-in/verify-login-otp");
                } else {

                    if(user.getFailedAttempt()>=1){
                        bruteForceProtection.resetFailedAttempts(username);
                    }

                    HttpSession session = request.getSession();

                    // Clean up
                    session.removeAttribute("PENDING_USER");
                    session.removeAttribute("TWO_FACTOR_OTP");

                    Locale userLocale = Locale.forLanguageTag(user.getLanguagePreference().toString());
                    localeResolver.setLocale(request, response, userLocale);

                    UserProfileDto userProfileDto = getUserProfileDto(user);
                    session.setAttribute("loggedInUser", userProfileDto);

                    for(Role role : user.getRoles()){
                        if(role.getName().equals("ROLE_MASTER_ADMIN")){
                            session.setAttribute("IS_MASTER_ADMIN", true);
                        }
                        if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_MASTER_ADMIN")) {
                            response.sendRedirect("/admin/dashboard");
                            return;
                        }
                    }

                    response.sendRedirect("/user/profile");
                }
            } else {

                if(user.getFailedAttempt()>=1){
                    bruteForceProtection.resetFailedAttempts(username);
                }

                Locale userLocale = Locale.forLanguageTag(user.getLanguagePreference().toString());
                localeResolver.setLocale(request, response, userLocale);

                UserProfileDto userProfileDto = getUserProfileDto(user);
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", userProfileDto);

                for(Role role : user.getRoles()){
                    if(role.getName().equals("ROLE_MASTER_ADMIN")){
                        session.setAttribute("IS_MASTER_ADMIN", true);
                    }
                    if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_MASTER_ADMIN")) {
                        response.sendRedirect("/admin/dashboard");
                        return;
                    }
                }

                response.sendRedirect("/user/profile");
            }
        }else{
            if(bruteForceProtection.unlockWhenTimeExpired(user)){

                Locale userLocale = Locale.forLanguageTag(user.getLanguagePreference().toString());
                localeResolver.setLocale(request, response, userLocale);

                UserProfileDto userProfileDto = getUserProfileDto(user);
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", userProfileDto);

                for(Role role : user.getRoles()){
                    if(role.getName().equals("ROLE_MASTER_ADMIN")){
                        session.setAttribute("IS_MASTER_ADMIN", true);
                    }
                    if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_MASTER_ADMIN")) {
                        response.sendRedirect("/admin/dashboard");
                        return;
                    }
                }

                response.sendRedirect("/user/profile");
            }else{
                request.getSession().invalidate();
                response.sendRedirect("/auth/sign-in?status=locked");
            }
        }
    }

    // Convert Entity to Dto
    private UserProfileDto getUserProfileDto(User user) {

        UserProfileDto userProfileDto = new UserProfileDto();

        userProfileDto.setId(user.getId());
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setPhone(user.getPhone());
        userProfileDto.setCity(user.getCity());
        userProfileDto.setState(user.getState());
        userProfileDto.setDob(user.getDateOfBirth());
        userProfileDto.setGender(user.getGender());
        userProfileDto.setLanguagePreference(user.getLanguagePreference());
        userProfileDto.setPasswordSet(user.isPasswordSet());
        userProfileDto.setTwoFactorEnabled(user.isTwoFactorEnabled());
        userProfileDto.setProfilePhotoUrl(user.getProfilePhotoUrl());
        userProfileDto.setSubscribed(user.isSubscribed());

        return userProfileDto;
    }
}
