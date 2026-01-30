package com.newsgroupmanagement.controller;

import com.newsgroupmanagement.dto.*;
import com.newsgroupmanagement.security.CustomLoginSuccessHandler;
import com.newsgroupmanagement.service.AuthService;
import com.newsgroupmanagement.service.CustomUserDetailsService;
import com.newsgroupmanagement.service.EmailService;
import com.newsgroupmanagement.util.OtpGenerator;
import com.newsgroupmanagement.validation.group.EmailValidationGroup;
import com.newsgroupmanagement.validation.group.NameValidationGroup;
import com.newsgroupmanagement.validation.group.OtpValidationGroup;
import com.newsgroupmanagement.validation.group.PasswordValidationGroup;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final CustomUserDetailsService userDetailsService;
    private final CustomLoginSuccessHandler successHandler;

    @Autowired
    public AuthController(AuthService authService, EmailService emailService, CustomUserDetailsService userDetailsService, CustomLoginSuccessHandler successHandler) {
        this.authService = authService;
        this.emailService = emailService;
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    // --- SignIn ---
    @GetMapping("/sign-in")
    public String viewSignIn(@RequestParam(required = false) String status, @RequestParam(required = false) String oauth2Error, @RequestParam(required = false) String email, HttpSession session, Model model) {

        if(status!=null && status.equals("notfound")) {
            model.addAttribute("email", session.getAttribute("FAILED_EMAIL"));
            model.addAttribute("authError", "Account not found.");
        } else if(status!=null && status.equals("locked")) {
            model.addAttribute("email", session.getAttribute("FAILED_EMAIL"));
            model.addAttribute("authError", "Too many failed attempts. Please try again in 5 minutes.");
        } else if(status!=null && status.equals("invalid")) {
            model.addAttribute("email", session.getAttribute("FAILED_EMAIL"));
            model.addAttribute("authError", "Incorrect credentials. Please try again.");
        } else if(status!=null && status.equals("disabled")) {
            model.addAttribute("email", session.getAttribute("FAILED_EMAIL"));
            model.addAttribute("authError", "Your account has been disabled. Please contact support.");
        } else if(status!=null && status.equals("deleted")) {
            model.addAttribute("email", session.getAttribute("FAILED_EMAIL"));
            model.addAttribute("authError", "Account deleted. Please contact support for assistance.");
        }

        if(oauth2Error != null && oauth2Error.equals("email_sign_in_required")) {
            model.addAttribute("oauth2Error", "Use email and password to sign in.");
        } else if(oauth2Error != null && oauth2Error.equals("account_disabled")) {
            model.addAttribute("oauth2Error","Your account has been disabled. Please contact support.");
        } else if(oauth2Error != null && oauth2Error.equals("deleted")) {
            model.addAttribute("oauth2Error","Account deleted. Please contact support for assistance.");
        }

        session.removeAttribute("FAILED_EMAIL");
        model.addAttribute("activeSignIn","emailAndPassword");
        return "auth/sign-in";
    }

    @PostMapping("/pre-login")
    public String preLoginValidation(@Validated({EmailValidationGroup.NotBlank.class,EmailValidationGroup.Size.class,EmailValidationGroup.Pattern.class,PasswordValidationGroup.NotBlank.class,PasswordValidationGroup.Pattern.class}) @ModelAttribute AuthRequestDto authRequestDto, BindingResult bindingResult, Model model) {

        // AuthRequestDto otp validation check
        if(bindingResult.hasErrors()) {
            model.addAttribute("email",authRequestDto.getEmail());
            model.addAttribute("password",authRequestDto.getPassword());
            model.addAttribute("activeSignIn", "emailAndPassword");
            model.addAttribute("errors", bindingResult);
            return "auth/sign-in";
        }

        // Redirect to Spring Security login processing URL
        return "forward:/auth/login";
    }

    @GetMapping("/sign-in/verify-login-otp")
    public String viewVerifySignInOtp(Model model) {
        model.addAttribute("activeSignIn","twoFactorEmail");
        return "auth/sign-in";
    }

    @PostMapping("/sign-in/verify-login-otp")
    public String verifySignInOtp(@Validated({OtpValidationGroup.NotBlank.class,OtpValidationGroup.Pattern.class}) @ModelAttribute AuthRequestDto authRequestDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws ServletException, IOException {

        // AuthRequestDto otp validation check
        if(bindingResult.hasErrors()) {
            model.addAttribute("twoFactorOtp",authRequestDto.getOtp());
            model.addAttribute("activeSignIn", "twoFactorEmail");
            model.addAttribute("errors", bindingResult);
            return "auth/sign-in";
        }

        String twoFactorOtp = (String)session.getAttribute("TWO_FACTOR_OTP");

        if(!twoFactorOtp.equals(authRequestDto.getOtp())) {
            model.addAttribute("twoFactorOtp",authRequestDto.getOtp());
            model.addAttribute("authError","Invalid OTP. Please try again.");
            model.addAttribute("activeSignIn","twoFactorEmail");
            return "auth/sign-in";
        }

        // Set OTP verified flag
        session.setAttribute("OTP_VERIFIED", true);

        // Get username
        String email = (String) session.getAttribute("PENDING_USER");

        // Load user
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Authenticate manually
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        // Session bind
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // Fire the default Spring Security success handler
        successHandler.onAuthenticationSuccess(request,response,auth);

        return null;
    }

    // --- SignUp ---
    @GetMapping("/sign-up")
    public String viewSignUp(Model model) {
        model.addAttribute("activeSignUp", "email");
        return "auth/sign-up";
    }

    @PostMapping("/sign-up/otp/send")
    public String sendSignupOtp(@Validated({EmailValidationGroup.NotBlank.class,EmailValidationGroup.Size.class,EmailValidationGroup.Pattern.class}) @ModelAttribute AuthRequestDto authRequestDto, BindingResult bindingResult, HttpSession session, Model model) {

        model.addAttribute("email", authRequestDto.getEmail());

        // AuthRequestDto email validation check
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult);
            model.addAttribute("activeSignUp", "email");
            return "auth/sign-up";
        }

        if(!authRequestDto.isTermsAccepted()){
            model.addAttribute("termsError", "You must agree to the Terms and Privacy Policy.");
            model.addAttribute("activeSignUp", "email");
            return "auth/sign-up";
        }

        if(authService.sendSignupOtp(authRequestDto.getEmail())){
            String otp = OtpGenerator.generateOtp();
            System.out.println(otp);
            authRequestDto.setOtp(otp);
            emailService.sendSignupOtpEmail(authRequestDto.getEmail(), authRequestDto.getOtp());
            session.setAttribute("authRequest", authRequestDto);
            model.addAttribute("activeSignUp", "otp");
            return "auth/sign-up";
        }
        model.addAttribute("activeSignUp", "email");
        model.addAttribute("authError", "The email address you entered is already in use.");
        return "auth/sign-up";
    }

    @PostMapping("/sign-up/otp/verify")
    public String verifySignupOtp(@Validated({OtpValidationGroup.NotBlank.class,OtpValidationGroup.Pattern.class}) @ModelAttribute AuthRequestDto authRequestDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws ServletException, IOException {

        model.addAttribute("otp",authRequestDto.getOtp());

        // AuthRequestDto otp validation check
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult);
            model.addAttribute("activeSignUp", "otp");
            return "auth/sign-up";
        }

        AuthRequestDto authRequest = (AuthRequestDto) session.getAttribute("authRequest");
        if(authRequest.getOtp()!=null && authRequest.getOtp().equals(authRequestDto.getOtp())){
            if(authService.checkUserExists(authRequest.getEmail())){
                authService.updateExistingUser(authRequest);
                emailService.sendLocalSignupWelcomeEmail(authRequest.getEmail());
                model.addAttribute("activeSignUp", "email");

                // Load user
                UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

                // Authenticate manually
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

                // Session bind
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

                // Fire the default Spring Security success handler
                successHandler.onAuthenticationSuccess(request,response,auth);
                return null;
            }
            model.addAttribute("activeSignUp", "name");
            return "auth/sign-up";
        }

        model.addAttribute("activeSignUp", "otp");
        model.addAttribute("authError","Invalid OTP. Please try again.");
        return "auth/sign-up";
    }

    @PostMapping("/sign-up/name")
    public String submitSignupName(@Validated({NameValidationGroup.NotBlank.class,NameValidationGroup.Pattern.class}) @ModelAttribute AuthRequestDto authRequestDto, BindingResult bindingResult, HttpSession session, Model model) {

        // AuthRequestDto name validation check
        if(bindingResult.hasErrors()) {
            model.addAttribute("firstName", authRequestDto.getFirstName());
            model.addAttribute("lastName", authRequestDto.getLastName());
            model.addAttribute("errors", bindingResult);
            model.addAttribute("activeSignUp", "name");
            return "auth/sign-up";
        }

        AuthRequestDto authRequest = (AuthRequestDto) session.getAttribute("authRequest");
        authRequest.setFirstName(authRequestDto.getFirstName());
        authRequest.setLastName(authRequestDto.getLastName());
        model.addAttribute("activeSignUp", "password");
        return "auth/sign-up";
    }

    @PostMapping("/sign-up/password")
    public String submitSignupPassword(@Validated({PasswordValidationGroup.NotBlank.class,PasswordValidationGroup.Pattern.class})@ModelAttribute AuthRequestDto authRequestDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws ServletException, IOException {

        if(!authRequestDto.getPassword().equals(authRequestDto.getConfirmPassword())){
            bindingResult.rejectValue("confirmPassword","password.mismatch","Passwords do not match");
        }

        // AuthRequestDto password validation check
        if(bindingResult.hasErrors()) {
            model.addAttribute("password", authRequestDto.getPassword());
            model.addAttribute("confirmPassword", authRequestDto.getConfirmPassword());
            model.addAttribute("errors", bindingResult);
            model.addAttribute("activeSignUp", "password");
            return "auth/sign-up";
        }

        AuthRequestDto authRequest = (AuthRequestDto) session.getAttribute("authRequest");
        authRequest.setPassword(authRequestDto.getPassword());
        authService.createNewUser(authRequest);
        emailService.sendLocalSignupWelcomeEmail(authRequest.getEmail());
        session.removeAttribute("authRequest");
        model.addAttribute("activeSignUp", "email");

        // Load user
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

        // Authenticate manually
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        // Session bind
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // Fire the default Spring Security success handler
        successHandler.onAuthenticationSuccess(request,response,auth);
        return null;
    }

    @PostMapping("/delete-account")
    public String deleteAccount(HttpSession session) {

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if(userProfileDto == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        authService.deleteUserAccount(userProfileDto.getEmail());
        session.invalidate();

        return "redirect:/auth/sign-in";
    }

    @PostMapping("/2fa/enable")
    @ResponseBody
    public Map<String, Object> enableTwoStepVerification(HttpSession session, Model model) {

        Map<String, Object> response = new HashMap<>();

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if (userProfileDto == null) {
            response.put("success", false);
            response.put("redirect", "/auth/sign-in");
            return response;
        }

        authService.enableTwoFactorAuth(userProfileDto.getEmail());
        userProfileDto.setTwoFactorEnabled(true);

        response.put("success", true);
        response.put("enabled", true);
        return response;
    }

    @PostMapping("/2fa/disable")
    @ResponseBody
    public Map<String, Object> disableTwoStepVerification(HttpSession session, Model model) {

        Map<String, Object> response = new HashMap<>();

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if (userProfileDto == null) {
            response.put("success", false);
            response.put("redirect", "/auth/sign-in");
            return response;
        }

        authService.disableTwoFactorAuth(userProfileDto.getEmail());
        userProfileDto.setTwoFactorEnabled(false);

        response.put("success", true);
        response.put("enabled", false);
        return response;
    }

    @PostMapping("/password/update")
    public String updatePassword(@Validated({PasswordValidationGroup.NotBlank.class,PasswordValidationGroup.Pattern.class}) @ModelAttribute ChangePasswordDto changePasswordDto, BindingResult bindingResult, HttpSession session, Model model){

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser",userProfileDto);

        if (userProfileDto == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        if(!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())){
            bindingResult.rejectValue("confirmNewPassword","password.mismatch","Passwords do not match");
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("oldPassword", changePasswordDto.getOldPassword());
            model.addAttribute("newPassword", changePasswordDto.getNewPassword());
            model.addAttribute("confirmNewPassword", changePasswordDto.getConfirmNewPassword());
            model.addAttribute("changePasswordErrors", bindingResult);
            return "user/user-profile";
        }

        if(!authService.verifyCurrentPassword(userProfileDto.getEmail(),changePasswordDto.getOldPassword())){
            model.addAttribute("oldPasswordInvalid","Old password is incorrect. Please try again.");
            model.addAttribute("oldPassword", changePasswordDto.getOldPassword());
            model.addAttribute("newPassword", changePasswordDto.getNewPassword());
            model.addAttribute("confirmNewPassword", changePasswordDto.getConfirmNewPassword());
            return "user/user-profile";
        }

        authService.updateUserPassword(userProfileDto.getEmail(),changePasswordDto.getNewPassword());
        model.addAttribute("passwordChangeSuccess","Password changed successfully!");

        return "user/user-profile";
    }

    @PostMapping("/password/set")
    public String setPassword(@Validated({PasswordValidationGroup.NotBlank.class,PasswordValidationGroup.Pattern.class}) @ModelAttribute SetPasswordDto setPasswordDto, BindingResult bindingResult, HttpSession session, Model model){

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser",userProfileDto);

        if (userProfileDto == null) {
            session.invalidate();
            return "redirect:/auth/sign-in";
        }

        if(!setPasswordDto.getPassword().equals(setPasswordDto.getConfirmNewPassword())){
            bindingResult.rejectValue("confirmNewPassword","password.mismatch","Passwords do not match");
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("password", setPasswordDto.getPassword());
            model.addAttribute("confirmNewPassword", setPasswordDto.getConfirmNewPassword());
            model.addAttribute("setPasswordErrors", bindingResult);
            return "user/user-profile";
        }

        authService.setPassword(userProfileDto.getEmail(),setPasswordDto.getPassword());
        userProfileDto.setPasswordSet(true);

        return "user/user-profile";
    }

    @GetMapping("/forgot-password")
    public String viewForgotPassword(Model model) {
        model.addAttribute("activeForgot","email");
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password/email/send")
    public String sendForgotPasswordOtp(@Validated({EmailValidationGroup.NotBlank.class,EmailValidationGroup.Size.class,EmailValidationGroup.Pattern.class}) @ModelAttribute ForgotPasswordEmailDto forgotPasswordEmailDto, BindingResult bindingResult, HttpSession session, Model model){

        model.addAttribute("email", forgotPasswordEmailDto.getEmail());

        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult);
            model.addAttribute("activeForgot", "email");
            return "auth/forgot-password";
        }

        if(!authService.checkUserExists(forgotPasswordEmailDto.getEmail())){
            model.addAttribute("forgotPasswordError", "Email not found.");
            model.addAttribute("activeForgot", "email");
            return "auth/forgot-password";
        }

        if(!authService.checkPasswordSet(forgotPasswordEmailDto.getEmail())){
            model.addAttribute("forgotPasswordError","You are not a local user. Please set a password in your profile.");
            model.addAttribute("activeForgot", "email");
            return "auth/forgot-password";
        }

        String otp = OtpGenerator.generateOtp();
        forgotPasswordEmailDto.setOtp(otp);
        emailService.sendForgotPasswordOtpEmail(forgotPasswordEmailDto.getEmail(),forgotPasswordEmailDto.getOtp());
        session.setAttribute("forgotPassword", forgotPasswordEmailDto);
        model.addAttribute("activeForgot","otp");
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password/otp/verify")
    public String verifyForgotPasswordOtp(@Validated({OtpValidationGroup.NotBlank.class, OtpValidationGroup.Pattern.class}) @ModelAttribute ForgotPasswordEmailDto forgotPasswordEmailDto, BindingResult bindingResult, HttpSession session, Model model) {

        model.addAttribute("otp", forgotPasswordEmailDto.getOtp());

        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult);
            model.addAttribute("activeForgot", "otp");
            return "auth/forgot-password";
        }

        ForgotPasswordEmailDto forgotPassword = (ForgotPasswordEmailDto) session.getAttribute("forgotPassword");
        if(forgotPasswordEmailDto.getOtp()!=null && forgotPasswordEmailDto.getOtp().equals(forgotPassword.getOtp())){
            model.addAttribute("activeForgot","resetPassword");
            return "auth/forgot-password";
        }

        model.addAttribute("activeForgot","otp");
        model.addAttribute("forgotPasswordError","Invalid OTP. Please try again.");
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password/reset")
    public String resetForgottenPassword(@Validated({PasswordValidationGroup.NotBlank.class, PasswordValidationGroup.Pattern.class}) @ModelAttribute ForgotPasswordEmailDto forgotPasswordEmailDto, BindingResult bindingResult, HttpSession session, Model model) {

        model.addAttribute("password", forgotPasswordEmailDto.getPassword());
        model.addAttribute("confirmPassword", forgotPasswordEmailDto.getConfirmPassword());

        if(!forgotPasswordEmailDto.getPassword().equals(forgotPasswordEmailDto.getConfirmPassword())){
            bindingResult.rejectValue("confirmPassword","password.mismatch","Passwords do not match");
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult);
            model.addAttribute("activeForgot", "resetPassword");
            return "auth/forgot-password";
        }

        ForgotPasswordEmailDto forgotPassword = (ForgotPasswordEmailDto) session.getAttribute("forgotPassword");

        authService.resetUserPassword(forgotPassword.getEmail(), forgotPasswordEmailDto.getPassword());

        // Clean up session attributes
        session.removeAttribute("forgotPassword");

        model.addAttribute("activeForgot","email");
        model.addAttribute("passwordResetSuccess","Password reset successful. Please sign in.");

        return "auth/forgot-password";
    }
}