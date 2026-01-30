package com.newsgroupmanagement.controller;

import com.newsgroupmanagement.dto.UserDto;
import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.service.AdminManagementService;
import com.newsgroupmanagement.service.AdminService;
import com.newsgroupmanagement.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/master/manage-admins")
public class AdminManagementController {

    private final AdminService adminService;
    private final AdminManagementService adminManagementService;
    private final AuthService authService;
    @Autowired
    public AdminManagementController(AdminService adminService, AdminManagementService adminManagementService, AuthService authService) {
        this.adminService = adminService;
        this.adminManagementService = adminManagementService;
        this.authService = authService;
    }

    @GetMapping
    public String viewManageAdmin(HttpSession session, Model model){

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
        List<UserDto> activeUser = adminManagementService.getAllActiveAdmins();
        model.addAttribute("activeUser", activeUser);
        return "/admin/manage-admins";
    }

    @GetMapping("/admin/search")
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
        List<UserDto> activeUser = adminManagementService.searchAdmins(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("activeUser", activeUser);
        return "admin/manage-admins";
    }

    @GetMapping("/add/admin")
    public String viewAddAdmin(HttpSession session, Model model){

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

        return "/admin/add-admin";
    }

    @PostMapping("/add/admin")
    public String addAdmin(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String phone, HttpSession session, Model model){

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
        if(authService.checkUserExists(email)){
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            model.addAttribute("errorMessage","An admin with this email already exists.");
            return "admin/add-admin";
        }

        adminManagementService.addAdmin(firstName, lastName, email, phone);
        model.addAttribute("successMessage","Admin added successfully!");
        return "/admin/add-admin";
    }

    @PostMapping("/edit/admin")
    public String viewAdminUserDetailsUpdate(@RequestParam("user_id") String id, Model model) {

        UserDto userDto = adminManagementService.getActiveAdminById(Long.parseLong(id));
        model.addAttribute("activeUser", userDto);

        return "admin/edit-admin";
    }

    @PostMapping("/update/admin")
    public String updateUserDetails(@ModelAttribute UserDto userDto, Model model){
        adminManagementService.updateAdmin(userDto);
        return "redirect:/master/manage-admins";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("user_id") String id){
        adminManagementService.deleteAdmin(Long.parseLong(id));
        return "redirect:/master/manage-admins";
    }

    @PostMapping("/admin/disable")
    public String disableUser(@RequestParam("user_id") String id){
        adminService.disableUser(Long.parseLong(id));
        return "redirect:/master/manage-admins";
    }

    @GetMapping("/manage-block-admins")
    public String viewManageBlockAdmins(HttpSession session, Model model){

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
        List<UserDto> blockAdmins = adminManagementService.getAllBlockAdmins();
        model.addAttribute("blockAdmins", blockAdmins);
        return "admin/manage-block-admins";
    }

    @GetMapping("/block/admin/search")
    public String searchBlockAdmins(@RequestParam("searchPattern") String searchPattern, HttpSession session, Model model){

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
        List<UserDto> blockAdmins = adminManagementService.searchBlockAdmins(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("blockAdmins", blockAdmins);
        return "admin/manage-block-admins";
    }

    @PostMapping("/admin/unblock")
    public String unBlockAdmin(@RequestParam("userId") Long userId, Model model){
        adminManagementService.unBlockAdmin(userId);
        return "redirect:/master/manage-admins/manage-block-admins";
    }

    @GetMapping("/manage-deleted-admins")
    public String viewManageDeletedAdmins(HttpSession session, Model model){

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
        List<UserDto> deletedAdmins = adminManagementService.getAllDeletedAdmins();
        model.addAttribute("deletedAdmins", deletedAdmins);
        return "admin/manage-deleted-admins";
    }

    @GetMapping("/deleted-admins/search")
    public String searchDeletedAdmins(@RequestParam("searchPattern") String searchPattern, HttpSession session, Model model){

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
        List<UserDto> deletedAdmins = adminManagementService.searchDeletedAdmins(searchPattern);
        model.addAttribute("searchPattern", searchPattern);
        model.addAttribute("deletedAdmins", deletedAdmins);
        return "admin/manage-deleted-admins";
    }

    @PostMapping("/admin/recover")
    public String adminAccountRecover(@RequestParam("userId") Long userId, Model model){
        adminManagementService.adminAccountRecover(userId);
        return "redirect:/master/manage-admins/manage-deleted-admins";
    }
}
