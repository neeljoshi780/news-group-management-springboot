package com.newsgroupmanagement.service;

import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.enums.LanguagePreference;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.UserRepository;
import com.newsgroupmanagement.util.FileOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileOperation fileOperation;
    @Autowired
    public UserService(UserRepository userRepository, FileOperation fileOperation) {
        this.userRepository = userRepository;
        this.fileOperation = fileOperation;
    }

    public void saveProfilePhoto(UserProfileDto userProfileDto, MultipartFile file) {

        String email = userProfileDto.getEmail();
        User user = userRepository.findByEmail(email).isPresent() ? userRepository.findByEmail(email).get() : null;

        if (user != null) {

            // delete old profile photo in storage
            String oldProfilePhoto = user.getProfilePhotoUrl();
            if(oldProfilePhoto!=null && !oldProfilePhoto.isEmpty()){
                fileOperation.deleteProfilePhoto(oldProfilePhoto);
            }

            // save new profile photo in storage
            String profilePhotoName = fileOperation.profilePhotoUpload(file);
            user.setProfilePhotoUrl(profilePhotoName);
            userRepository.save(user);
            userProfileDto.setProfilePhotoUrl(profilePhotoName);
        }
    }

    public void updateUserProfile(UserProfileDto userProfileDto) {

        User user = userRepository.findByEmail(userProfileDto.getEmail()).orElse(null);

        if (user != null) {
            user.setFirstName(userProfileDto.getFirstName());
            user.setLastName(userProfileDto.getLastName());
            user.setPhone(userProfileDto.getPhone());
            user.setCity(userProfileDto.getCity());
            user.setState(userProfileDto.getState());
            user.setDateOfBirth(userProfileDto.getDob());
            user.setGender(userProfileDto.getGender());
            userRepository.save(user);
        }
    }

    public void updateLanguagePreference(String email,String lang){

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && isValidLanguage(lang) ) {
            user.setLanguagePreference(LanguagePreference.valueOf(lang.toUpperCase()));
            userRepository.save(user);
        }
    }

    private boolean isValidLanguage(String lang) {
        try {
            LanguagePreference.valueOf(lang.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
