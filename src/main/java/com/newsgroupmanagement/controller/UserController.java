package com.newsgroupmanagement.controller;

import com.newsgroupmanagement.dto.CategoryDto;
import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.UserRepository;
import com.newsgroupmanagement.service.CategoryService;
import com.newsgroupmanagement.service.UserService;
import com.newsgroupmanagement.util.FileOperation;
import com.newsgroupmanagement.validation.UserProfileValidator;
import com.newsgroupmanagement.validation.group.NameValidationGroup;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String viewUserProfile(HttpSession session, Model model) {

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if(userProfileDto == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        model.addAttribute("loggedInUser", userProfileDto);

        return "user/user-profile";
    }

    @PostMapping("/profile-photo/update")
    public String updateProfilePhoto(@RequestParam("profile-photo") MultipartFile file, Model model, HttpSession session) {

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if(userProfileDto == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        model.addAttribute("loggedInUser", userProfileDto);

        // profile photo validation
        if(file.isEmpty()) {
            model.addAttribute("profileError", "Please select a profile photo to upload.");
            return "user/user-profile";
        }

        String contentTypeOfProfile = file.getContentType();
        if(!contentTypeOfProfile.equals("image/jpg") && !contentTypeOfProfile.equals("image/jpeg") && !contentTypeOfProfile.equals("image/png")){
            model.addAttribute("profileError", "Please select a valid image file (JPG, JPEG, PNG).");
            return "user/user-profile";
        }

        long maxProfileSize = 2 * 1024*1024;
        long profileSize = file.getSize();
        if(profileSize > maxProfileSize) {
            model.addAttribute("profileError", "File size exceeds 2MB limit.");
            return "user/user-profile";
        }

        userService.saveProfilePhoto(userProfileDto,file);

        return "redirect:/user/profile";
    }

    @PostMapping("/profile-info/update")
    public String updateUserProfile(@Validated({NameValidationGroup.Pattern.class}) @ModelAttribute UserProfileDto userProfileDto, BindingResult bindingResult,HttpSession session, Model model) {

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Map<String,String> profileErrors = UserProfileValidator.validateProfileFields(userProfileDto);
        if(!profileErrors.isEmpty()) {
            model.addAttribute("errors", profileErrors);
            model.addAttribute("loggedInUser", userProfileDto);
            return "user/user-profile";
        }

        String email = user.getEmail();
        String updatedEmail = userProfileDto.getEmail();
        if(!email.equals(updatedEmail)) {
            model.addAttribute("emailChangeError", "Email change is not allowed.");
            model.addAttribute("loggedInUser", userProfileDto);
            return "user/user-profile";
        }

        userService.updateUserProfile(userProfileDto);
        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setPhone(userProfileDto.getPhone());
        user.setCity(userProfileDto.getCity());
        user.setState(userProfileDto.getState());
        user.setDob(userProfileDto.getDob());
        user.setGender(userProfileDto.getGender());

        return "redirect:/user/profile";
    }

    @GetMapping("/share-news-form")
    public String viewNewsShareForm(){
        return "user/news-share";
    }
}
