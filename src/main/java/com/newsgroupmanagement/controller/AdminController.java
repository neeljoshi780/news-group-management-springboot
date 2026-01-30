package com.newsgroupmanagement.controller;

import com.newsgroupmanagement.dto.*;
import com.newsgroupmanagement.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public String viewDashboard(HttpSession session, Model model) {

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if(userProfileDto == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");

        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);

        String formattedDate = today.format(formatter);

        long totalMasterAdmin = adminService.getTotalMasterAdmins();
        long totalAdmin = adminService.getTotalAdmins();
        long totalUsers = adminService.getTotalUsers();
        long totalPosts = adminService.getTotalPosts();

        List<RecentNewsDto> recentNewsList = adminService.getRecentActivities();

        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", userProfileDto);
        model.addAttribute("formattedDate", formattedDate);
        model.addAttribute("totalMasterAdmin", totalMasterAdmin);
        model.addAttribute("totalAdmin", totalAdmin);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("recentNewsList", recentNewsList);

        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String viewAdminProfile(HttpSession session, Model model) {

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if(userProfileDto == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", userProfileDto);

        return "admin/admin-profile";
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
            return "admin/admin-profile";
        }

        String contentTypeOfProfile = file.getContentType();
        if(!contentTypeOfProfile.equals("image/jpg") && !contentTypeOfProfile.equals("image/jpeg") && !contentTypeOfProfile.equals("image/png")){
            model.addAttribute("profileError", "Please select a valid image file (JPG, JPEG, PNG).");
            return "admin/admin-profile";
        }

        long maxProfileSize = 2 * 1024*1024;
        long profileSize = file.getSize();
        if(profileSize > maxProfileSize) {
            model.addAttribute("profileError", "File size exceeds 2MB limit.");
            return "admin/admin-profile";
        }

        adminService.saveProfilePhoto(userProfileDto,file);

        return "redirect:/admin/profile";
    }

    @PostMapping("/profile-info/update")
    public String updateUserProfile(UserProfileDto userProfileDto, HttpSession session){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        adminService.updateAdminProfile(userProfileDto);
        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setPhone(userProfileDto.getPhone());
        user.setCity(userProfileDto.getCity());
        user.setState(userProfileDto.getState());
        user.setDob(userProfileDto.getDob());
        user.setGender(userProfileDto.getGender());

        return "redirect:/admin/profile";
    }

    @GetMapping("/manage-users")
    public String viewManageUser(HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<UserDto> activeUser = adminService.getAllActiveUsers();
        model.addAttribute("activeUser", activeUser);
        return "admin/manage-users";
    }

    @GetMapping("/user/search")
    public String searchUser(@RequestParam("searchPattern") String searchPattern, HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<UserDto> activeUser = adminService.searchUsers(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("activeUser", activeUser);
        return "admin/manage-users";
    }

    @PostMapping("/edit/user")
    public String viewAdminUserDetailsUpdate(@RequestParam("user_id") String id, Model model) {

        UserDto userDto = adminService.getActiveUserById(Long.parseLong(id));
        model.addAttribute("activeUser", userDto);

        return "admin/edit-user";
    }

    @PostMapping("/update/user")
    public String updateUserDetails(@ModelAttribute UserDto userDto, Model model){
        adminService.updateUser(userDto);
        return "redirect:/admin/manage-users";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam("user_id") String id){
        adminService.deleteUser(Long.parseLong(id));
        return "redirect:/admin/manage-users";
    }

    @PostMapping("/user/disable")
    public String disableUser(@RequestParam("user_id") String id){
        adminService.disableUser(Long.parseLong(id));
        return "redirect:/admin/manage-users";
    }

    @GetMapping("/manage-news")
    public String viewManageNews(HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<NewsDto> news = adminService.getAllNews();
        model.addAttribute("news", news);
        return "admin/manage-news";
    }

    @GetMapping("/news/search")
    public String searchNews(@RequestParam("searchPattern") String searchPattern, HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<NewsDto> news = adminService.searchNews(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("news", news);
        return "admin/manage-news";
    }

    @PostMapping("news/delete")
    public String deleteNews(@RequestParam("news_id") String id){
        adminService.deleteNews(Long.parseLong(id));
        return "redirect:/admin/manage-news";
    }

    @GetMapping("/manage-category")
    public String viewManageCategory(HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<CategoryDto> categories = adminService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/manage-category";
    }

    @GetMapping("/category/search")
    public String searchCategory(@RequestParam("searchPattern") String searchPattern, HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<CategoryDto> categories = adminService.searchCategory(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("categories", categories);
        return "admin/manage-category";
    }

    @GetMapping("/add/category")
    public String viewAddCategory(HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);

        return "admin/add-category";
    }

    @PostMapping("/add/category")
    public String addCategory(@RequestParam("categoryName") String categoryName, @RequestParam("categoryDescription") String categoryDescription, HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);

        adminService.createNewCategory(categoryName, categoryDescription);
        model.addAttribute("success", "Category added successful.");
        return "admin/add-category";
    }

    @PostMapping("/edit/category")
    public String editCategory(@RequestParam("categoryId") Long categoryId, HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);

        CategoryDto categoryDto = adminService.getCategoryById(categoryId);
        if(categoryDto == null){
            return "redirect:/admin/manage-category";
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("category", categoryDto);
        return "admin/edit-category";
    }

    @PostMapping("/update/category")
    public String updateCategory(@RequestParam("categoryName") String categoryName, @RequestParam("categoryDescription") String categoryDescription, @RequestParam("categoryId") Long categoryId, Model model){
        adminService.updateCategory(categoryName, categoryDescription, categoryId);
        return "redirect:/admin/manage-category";
    }

    @PostMapping("/category/delete")
    public String deleteCategory(@RequestParam("categoryId") String categoryId){
        adminService.deleteCategory(Long.parseLong(categoryId));
        return "redirect:/admin/manage-category";
    }

    @GetMapping("/manage-block-users")
    public String viewManageBlockUsers(HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<UserDto> blockUsers = adminService.getAllBlockUsers();
        model.addAttribute("blockUsers", blockUsers);
        return "admin/manage-block-users";
    }

    @GetMapping("/block/user/search")
    public String searchBlockUsers(@RequestParam("searchPattern") String searchPattern, HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<UserDto> blockUsers = adminService.searchBlockUsers(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("blockUsers", blockUsers);
        return "admin/manage-block-users";
    }

    @PostMapping("/user/unblock")
    public String unBlockUser(@RequestParam("userId") Long userId, Model model){
        adminService.unBlockUser(userId);
        return "redirect:/admin/manage-block-users";
    }

    @GetMapping("/manage-deleted-users")
    public String viewManageDeletedUsers(HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<UserDto> deletedUsers = adminService.getAllDeletedUsers();
        model.addAttribute("deletedUsers", deletedUsers);
        return "admin/manage-deleted-users";
    }

    @GetMapping("/deleted-users/search")
    public String searchDeletedUsers(@RequestParam("searchPattern") String searchPattern, HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<UserDto> deletedUsers = adminService.searchDeletedUsers(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("deletedUsers", deletedUsers);
        return "admin/manage-deleted-users";
    }

    @PostMapping("/user/recover")
    public String userAccountRecover(@RequestParam("userId") Long userId, Model model){
        adminService.userAccountRecover(userId);
        return "redirect:/admin/manage-deleted-users";
    }

    @GetMapping("/manage-contact")
    public String viewManageContact(HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<ContactDto> contacts = adminService.getAllContacts();
        model.addAttribute("contacts", contacts);
        return "admin/manage-contacts";
    }

    @GetMapping("/contact/search")
    public String searchContact(@RequestParam("searchPattern") String searchPattern, HttpSession session, Model model){

        UserProfileDto user = (UserProfileDto) session.getAttribute("loggedInUser");

        if(user == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        Boolean IS_MASTER_ADMIN = (Boolean) session.getAttribute("IS_MASTER_ADMIN");
        if(IS_MASTER_ADMIN != null){
            model.addAttribute("IS_MASTER_ADMIN", true);
        }

        model.addAttribute("loggedInUser", user);
        List<ContactDto> contacts = adminService.searchContacts(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("contacts", contacts);
        return "admin/manage-contacts";
    }

    @PostMapping("/delete/contact")
    public String deleteContact(@RequestParam("contactId") Long contactId){
        adminService.deleteContact(contactId);
        return "redirect:/admin/manage-contact";
    }
}
