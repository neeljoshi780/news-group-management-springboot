package com.newsgroupmanagement.security;

import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class BruteForceProtection {

    private final UserRepository userRepository;
    public BruteForceProtection(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 5;

    public void increaseFailedAttempts(User user) {
        int newAttempts = user.getFailedAttempt()+1;
        user.setFailedAttempt(newAttempts);
        if(newAttempts >= MAX_ATTEMPTS) {
            user.setAccountNonLocked(false);
            user.setLockTime(LocalDateTime.now());
        }
        userRepository.save(user);
    }

    public void resetFailedAttempts(String email) {
        User user = userRepository.findByEmail(email).isPresent() ? userRepository.findByEmail(email).get() : null;
        if(user != null) {
            user.setFailedAttempt(0);
            userRepository.save(user);
        }
    }

    public boolean unlockWhenTimeExpired(User user) {
        if(user.getLockTime()==null) return false;

        Duration lockDuration = Duration.between(user.getLockTime(), LocalDateTime.now());
        if(lockDuration.toMinutes() >= LOCK_TIME_DURATION) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
