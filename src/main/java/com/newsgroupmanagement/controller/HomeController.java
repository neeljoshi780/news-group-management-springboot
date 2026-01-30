package com.newsgroupmanagement.controller;

import com.newsgroupmanagement.dto.CategoryDto;
import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.service.CategoryService;
import com.newsgroupmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@Controller
public class HomeController {

    private final UserService userService;
    private final CategoryService categoryService;
    public HomeController(UserService userService,CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String lang, Model model, Locale locale,HttpSession session) {
        LocalDate date = LocalDate.now();
        // Automatically format date based on the selected locale
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale);
        String formattedDate = date.format(formatter);

        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if(userProfileDto != null && lang != null) {
            userService.updateLanguagePreference(userProfileDto.getEmail(),lang);
        }

        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("loggedInUser",userProfileDto);
        model.addAttribute("formattedDate", formattedDate);
        return "index";
    }
}
