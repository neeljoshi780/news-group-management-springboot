package com.newsgroupmanagement.service;

import com.newsgroupmanagement.dto.*;
import com.newsgroupmanagement.model.Category;
import com.newsgroupmanagement.model.Contact;
import com.newsgroupmanagement.model.News;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.CategoryRepository;
import com.newsgroupmanagement.repository.ContactRepository;
import com.newsgroupmanagement.repository.NewsRepository;
import com.newsgroupmanagement.repository.UserRepository;
import com.newsgroupmanagement.util.FileOperation;
import com.newsgroupmanagement.util.TimeAgo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final ContactRepository contactRepository;
    private final FileOperation fileOperation;
    private final ModelMapper modelMapper;
    @Autowired
    public AdminService(UserRepository userRepository, NewsRepository newsRepository, CategoryRepository categoryRepository, ContactRepository contactRepository, FileOperation fileOperation, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.contactRepository = contactRepository;
        this.fileOperation = fileOperation;
        this.modelMapper = modelMapper;
    }

    // Convert User entity to UserDto
    public UserDto convertToDTO(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    // Convert Category entity to CategoryDto
    public CategoryDto convertToDTO(Category category) {return modelMapper.map(category, CategoryDto.class);}

    // Convert Contact entity to ContactDto
    public ContactDto convertToDTO(Contact contact) {return modelMapper.map(contact, ContactDto.class);}

    public long getTotalMasterAdmins(){
        return userRepository.countByMasterAdmin();
    }

    public long getTotalAdmins(){
        return userRepository.countByAdmin();
    }

    public long getTotalUsers(){
        return userRepository.countByUser();
    }

    public long getTotalPosts(){
        return newsRepository.countByNews();
    }

    public List<RecentNewsDto> getRecentActivities(){
        List<RecentNewsDto> recentNews = newsRepository.findRecentTop5();

        return recentNews.stream().map(news -> {
            String timeAgo = TimeAgo.toTimeAgo(news.getCreatedAt().toLocalDateTime());
            news.setTimeAgo(timeAgo);
            return news;
        }).collect(Collectors.toList());
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

    public void updateAdminProfile(UserProfileDto userProfileDto) {

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

    public List<UserDto> getAllActiveUsers() {
        List<User> users = userRepository.findByUserIsDeletedFalseAndEnabledTrue();
        List<UserDto> activeUsers = new ArrayList<>();

        for(User user : users){
            activeUsers.add(convertToDTO(user));
        }

        return activeUsers;
    }

    public List<UserDto> searchUsers(String searchPattern) {
        List<User> users = userRepository.searchByUser("%"+searchPattern+"%");

        List<UserDto> activeUsers = new ArrayList<>();

        for(User user : users){
            activeUsers.add(convertToDTO(user));
        }

        return activeUsers;
    }

    public UserDto getActiveUserById(Long id) {
        Optional<User> user = userRepository.findActiveUserById(id);
        if(user.isPresent()){
            return convertToDTO(user.get());
        }
        return null;
    }

    public void updateUser(UserDto userDto) {
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
    public void deleteUser(Long id) {
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

    public void disableUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user != null){
            user.setEnabled(false);
            userRepository.save(user);
        }
    }

    public List<NewsDto> getAllNews(){
        return newsRepository.findAllNews();
    }

    public List<NewsDto> searchNews(String searchPattern) {
        return newsRepository.searchNews("%"+searchPattern+"%");
    }

    public void deleteNews(Long id) {
        News news = newsRepository.findById(id).orElse(null);
        if(news != null){
            fileOperation.deleteNewsPhoto(news.getImageUrl());
        }
        newsRepository.deleteById(id);
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDto = new ArrayList<>();

        for(Category category : categories){
            categoryDto.add(convertToDTO(category));
        }
        return categoryDto;
    }

    public List<CategoryDto> searchCategory(String searchPattern) {
        List<Category> categories = categoryRepository.searchCategory("%"+searchPattern+"%");
        List<CategoryDto> categoryDto = new ArrayList<>();

        for(Category category : categories){
            categoryDto.add(convertToDTO(category));
        }
        return categoryDto;
    }

    public void createNewCategory(String categoryName, String categoryDescription) {
        Category category = new Category();
        category.setName(categoryName);
        category.setDescription(categoryDescription);
        categoryRepository.save(category);
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null){
            return convertToDTO(category);
        }
        return null;
    }

    public void updateCategory(String categoryName, String categoryDescription, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if(category != null){
            category.setName(categoryName);
            category.setDescription(categoryDescription);
            categoryRepository.save(category);
        }
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<UserDto> getAllBlockUsers(){
        List<User> users = userRepository.findByUserIsDeletedFalseAndEnabledFalse();
        List<UserDto> blockUsers = new ArrayList<>();

        for(User user : users){
            blockUsers.add(convertToDTO(user));
        }

        return blockUsers;
    }

    public List<UserDto> searchBlockUsers(String searchPattern) {
        List<User> users = userRepository.searchByBlockUsers("%"+searchPattern+"%");

        List<UserDto> blockUsers = new ArrayList<>();

        for(User user : users){
            blockUsers.add(convertToDTO(user));
        }

        return blockUsers;
    }

    public void unBlockUser(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    public List<UserDto> getAllDeletedUsers(){
        List<User> users = userRepository.findByUserIsDeletedTrue();

        List<UserDto> deletedUsers = new ArrayList<>();

        for(User user : users){
            deletedUsers.add(convertToDTO(user));
        }

        return deletedUsers;
    }

    public List<UserDto> searchDeletedUsers(String searchPattern) {
        List<User> users = userRepository.searchByDeletedUsers("%"+searchPattern+"%");

        List<UserDto> deletedUsers = new ArrayList<>();

        for(User user : users){
            deletedUsers.add(convertToDTO(user));
        }

        return deletedUsers;
    }

    public void userAccountRecover(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.setDeleted(false);
            userRepository.save(user);
        }
    }

    public List<ContactDto> getAllContacts(){
        List<Contact> contacts = contactRepository.findAll();
        List<ContactDto> contactDto = new ArrayList<>();

        for(Contact contact : contacts){
            contactDto.add(convertToDTO(contact));
        }
        return contactDto;
    }

    public List<ContactDto> searchContacts(String searchPattern) {
        List<Contact> contacts = contactRepository.searchByContacts("%"+searchPattern+"%");
        List<ContactDto> contactDto = new ArrayList<>();

        for(Contact contact : contacts){
            contactDto.add(convertToDTO(contact));
        }
        return contactDto;
    }

    public void deleteContact(Long contactId) {
        contactRepository.deleteById(contactId);
    }
}
