package com.newsgroupmanagement.controller;

import com.newsgroupmanagement.dto.ContactDto;
import com.newsgroupmanagement.model.Contact;
import com.newsgroupmanagement.validation.group.EmailValidationGroup;
import com.newsgroupmanagement.validation.group.MessageValidation;
import com.newsgroupmanagement.validation.group.NameValidationGroup;
import com.newsgroupmanagement.validation.group.PhoneValidation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/about")
    public String viewAbout(){
        return "public/about-us";
    }

    @GetMapping("/terms")
    public String viewTerms(){
        return "public/terms-conditions";
    }

    @GetMapping("/privacy-policy")
    public String viewPrivacyPolicy(){
        return "public/privacy-policy";
    }
}
