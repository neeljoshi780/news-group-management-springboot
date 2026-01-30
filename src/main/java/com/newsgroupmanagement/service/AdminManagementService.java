package com.newsgroupmanagement.service;

import com.newsgroupmanagement.dto.UserDto;
import com.newsgroupmanagement.enums.AuthProvider;
import com.newsgroupmanagement.model.News;
import com.newsgroupmanagement.model.Role;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.NewsRepository;
import com.newsgroupmanagement.repository.RoleRepository;
import com.newsgroupmanagement.repository.UserRepository;
import com.newsgroupmanagement.util.FileOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final FileOperation fileOperation;
    @Autowired
    public AdminManagementService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, FileOperation fileOperation) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.fileOperation = fileOperation;
    }

    // Convert User entity to UserDto
    public UserDto convertToDTO(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public void addAdmin(String firstName, String lastName, String email, String phone){
        User admin = new User();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPhone(phone);
        admin.setPassword(passwordEncoder.encode("temp@123"));
        admin.setTermsAcceptedAt(LocalDateTime.now());
        admin.setAuthProvider(AuthProvider.LOCAL);

        Optional<Role> role = roleRepository.findByName("ROLE_ADMIN");
        if(role.isPresent()){
            admin.getRoles().add(role.get());
        }

        userRepository.save(admin);
    }

    public UserDto getActiveAdminById(Long id) {
        Optional<User> user = userRepository.findActiveUserById(id);
        if(user.isPresent()){
            return convertToDTO(user.get());
        }
        return null;
    }

    public void updateAdmin(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);

        if(user != null){
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setCity(userDto.getCity());
            user.setState(userDto.getState());
            user.setDateOfBirth(userDto.getDateOfBirth());
            user.setGender(userDto.getGender());
            user.setAuthProvider(userDto.getAuthProvider());
            user.setLanguagePreference(userDto.getLanguagePreference());
            user.setEnabled(userDto.isEnabled());
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteAdmin(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            fileOperation.deleteProfilePhoto(user.getProfilePhotoUrl());
            Set<News> news = user.getNewsList();
            for(News temp : news){
                fileOperation.deleteNewsPhoto(temp.getImageUrl());
            }
        }
        userRepository.deleteById(id);
    }

    public List<UserDto> getAllActiveAdmins() {
        List<User> users = userRepository.findByAdminIsDeletedFalseAndEnabledTrue();
        List<UserDto> activeUsers = new ArrayList<>();

        for(User user : users){
            activeUsers.add(convertToDTO(user));
        }

        return activeUsers;
    }

    public List<UserDto> searchAdmins(String searchPattern) {
        List<User> users = userRepository.searchByAdmins("%"+searchPattern+"%");

        List<UserDto> activeUsers = new ArrayList<>();

        for(User user : users){
            activeUsers.add(convertToDTO(user));
        }

        return activeUsers;
    }

    public List<UserDto> getAllBlockAdmins(){
        List<User> admins = userRepository.findByAdminIsDeletedFalseAndEnabledFalse();
        List<UserDto> blockAdmins = new ArrayList<>();

        for(User user : admins){
            blockAdmins.add(convertToDTO(user));
        }

        return blockAdmins;
    }

    public List<UserDto> searchBlockAdmins(String searchPattern) {
        List<User> admins = userRepository.searchByBlockAdmins("%"+searchPattern+"%");

        List<UserDto> blockAdmins = new ArrayList<>();

        for(User user : admins){
            blockAdmins.add(convertToDTO(user));
        }

        return blockAdmins;
    }

    public void unBlockAdmin(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    public List<UserDto> getAllDeletedAdmins(){
        List<User> users = userRepository.findByAdminIsDeletedTrue();

        List<UserDto> deletedAdmins = new ArrayList<>();

        for(User user : users){
            deletedAdmins.add(convertToDTO(user));
        }

        return deletedAdmins;
    }

    public List<UserDto> searchDeletedAdmins(String searchPattern) {
        List<User> users = userRepository.searchByDeletedAdmins("%"+searchPattern+"%");

        List<UserDto> deletedAdmins = new ArrayList<>();

        for(User user : users){
            deletedAdmins.add(convertToDTO(user));
        }

        return deletedAdmins;
    }

    public void adminAccountRecover(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.setDeleted(false);
            userRepository.save(user);
        }
    }
}
