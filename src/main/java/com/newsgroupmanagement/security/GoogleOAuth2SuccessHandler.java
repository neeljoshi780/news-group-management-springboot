package com.newsgroupmanagement.security;

import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.UserRepository;
import com.newsgroupmanagement.service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final LocaleResolver localeResolver;
    @Autowired
    public GoogleOAuth2SuccessHandler(UserRepository userRepository,LocaleResolver localeResolver) {
        this.userRepository = userRepository;
        this.localeResolver = localeResolver;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String username = defaultOAuth2User.getAttribute("email");

        Optional<User> optionalUser = userRepository.findByEmail(username);

        if(optionalUser.isEmpty()){
            response.sendRedirect("/auth/sign-in?status=notfound");
            return;
        }

        User user = optionalUser.get();

        if(user.isDeleted()){
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            response.sendRedirect("/auth/sign-in?oauth2Error=deleted");
            return;
        }

        Locale userLocale = Locale.forLanguageTag(user.getLanguagePreference().toString());
        localeResolver.setLocale(request, response, userLocale);

        UserProfileDto userProfileDto = getUserProfileDto(user);
        HttpSession session = request.getSession();
        session.setAttribute("loggedInUser", userProfileDto);

        response.sendRedirect("/user/profile");
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