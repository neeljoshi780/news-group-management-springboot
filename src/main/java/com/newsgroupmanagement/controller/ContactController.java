package com.newsgroupmanagement.controller;

import com.newsgroupmanagement.dto.ContactDto;
import com.newsgroupmanagement.service.ContactService;
import com.newsgroupmanagement.validation.group.EmailValidationGroup;
import com.newsgroupmanagement.validation.group.MessageValidation;
import com.newsgroupmanagement.validation.group.NameValidationGroup;
import com.newsgroupmanagement.validation.group.PhoneValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;
    @Autowired
    public ContactController(ContactService contactService){
        this.contactService = contactService;
    }

    @GetMapping("")
    public String viewContact(){
        return "public/contact-us";
    }

    @PostMapping("")
    public String saveContact(@Validated({NameValidationGroup.NotBlank.class, NameValidationGroup.Pattern.class, EmailValidationGroup.NotBlank.class, EmailValidationGroup.Size.class, EmailValidationGroup.Pattern.class, PhoneValidation.NotBlank.class, PhoneValidation.Pattern.class, MessageValidation.NotBlank.class, MessageValidation.Pattern.class}) @ModelAttribute ContactDto contactDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("errors", bindingResult);
            model.addAttribute("contact", contactDto);
            return "public/contact-us";
        }

        contactService.saveContact(contactDto);
        model.addAttribute("successContact", "Your inquiry has been received. Our team will contact you soon.");
        return "public/contact-us";
    }
}
