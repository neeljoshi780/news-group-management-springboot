package com.newsgroupmanagement.service;

import com.newsgroupmanagement.dto.AuthRequestDto;
import com.newsgroupmanagement.model.Role;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.RoleRepository;
import com.newsgroupmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- SignUp ---
    public boolean checkUserExists(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean sendSignupOtp(String email){
        boolean exists = checkUserExists(email);
        if(exists){
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                for(Role tempRole : user.getRoles()){
                    if(tempRole.getName().equals("ROLE_USER")){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void updateExistingUser(AuthRequestDto authRequestDto){
        if(authRequestDto.isTermsAccepted()){
            Optional<User> optionalUser = userRepository.findByEmail(authRequestDto.getEmail());
            Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
            Role  role = null;

            if(optionalRole.isPresent()){
                role = optionalRole.get();
                if(optionalUser.isPresent()){
                    User user = optionalUser.get();
                    user.getRoles().add(role);
                    userRepository.save(user);
                }
            }
        }
    }

    public void createNewUser(AuthRequestDto authRequestDto){
        if(authRequestDto.isTermsAccepted()){
            Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
            Role  role = null;

            if(optionalRole.isPresent()){
                role = optionalRole.get();
                User user = new User();
                user.setFirstName(authRequestDto.getFirstName());
                user.setLastName(authRequestDto.getLastName());
                user.setEmail(authRequestDto.getEmail());
                user.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));
                user.setTermsAcceptedAt(LocalDateTime.now());
                user.getRoles().add(role);
                userRepository.save(user);
            }
        }
    }

    public void deleteUserAccount(String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setDeleted(true);
            userRepository.save(user);
        }
    }

    public void enableTwoFactorAuth(String email){

        User user = userRepository.findByEmail(email).orElse(null);

        if(user!=null){
            user.setTwoFactorEnabled(true);
            userRepository.save(user);
        }
    }

    public void disableTwoFactorAuth(String email){

        User user = userRepository.findByEmail(email).orElse(null);

        if(user!=null){
            user.setTwoFactorEnabled(false);
            userRepository.save(user);
        }
    }

    public boolean verifyCurrentPassword(String email, String plainPassword) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(plainPassword, user.getPassword());
    }

    public void updateUserPassword(String email, String newPassword){
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    public void setPassword(String email, String setNewPassword){

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setPassword(passwordEncoder.encode(setNewPassword));
            user.setPasswordSet(true);
            userRepository.save(user);
        }
    }

    public boolean checkPasswordSet(String email){
        return userRepository.isPasswordSetByEmail(email);
    }

    public void resetUserPassword(String email, String newPassword){

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }
}
