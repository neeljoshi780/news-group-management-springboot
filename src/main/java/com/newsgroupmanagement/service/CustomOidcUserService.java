package com.newsgroupmanagement.service;

import com.newsgroupmanagement.enums.AuthProvider;
import com.newsgroupmanagement.exception.OAuth2AuthenticationProcessingException;
import com.newsgroupmanagement.model.Role;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.RoleRepository;
import com.newsgroupmanagement.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    public CustomOidcUserService(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {

        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(request);

        User user = createUser(oidcUser.getGivenName(),oidcUser.getFamilyName(),oidcUser.getEmail());

        if(!user.isEnabled()){
            throw new DisabledException("Your account has been disabled.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

    private User createUser(String firstName, String lastName, String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.isPasswordSet()){
                throw new OAuth2AuthenticationProcessingException("Use email and password to sign in.");
            }
            return user;
        }

        // new user
        User googleUser = new User();
        googleUser.setFirstName(firstName);
        googleUser.setLastName(lastName);
        googleUser.setEmail(email);
        googleUser.setPasswordSet(false);
        googleUser.setTermsAcceptedAt(LocalDateTime.now());
        googleUser.setAuthProvider(AuthProvider.GOOGLE);
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        optionalRole.ifPresent(role -> googleUser.getRoles().add(role));
        User user = userRepository.save(googleUser);
        emailService.sendGoogleSignupWelcomeEmail(email);

        return user;
    }
}