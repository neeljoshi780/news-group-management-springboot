package com.newsgroupmanagement.controller;

import com.newsgroupmanagement.dto.CategoryDto;
import com.newsgroupmanagement.dto.NewsListDto;
import com.newsgroupmanagement.dto.NewsPostRequestDto;
import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.service.CategoryService;
import com.newsgroupmanagement.service.NewsService;
import com.newsgroupmanagement.validation.group.CategoryValidationGroup;
import com.newsgroupmanagement.validation.group.DescriptionValidationGroup;
import com.newsgroupmanagement.validation.group.TitleValidationGroup;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/news")
public class NewsController {

    private final CategoryService categoryService;
    private final NewsService newsService;
    @Autowired
    public NewsController(CategoryService categoryService, NewsService newsService) {
        this.categoryService = categoryService;
        this.newsService = newsService;
    }

    @GetMapping("/share")
    public String viewNewsShareForm(Model model){
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "user/news-share";
    }

    @PostMapping("/share")
    @ResponseBody
    public Map<String, Object> shareNewsPost(@Validated({CategoryValidationGroup.NotBlank.class, TitleValidationGroup.NotBlank.class, TitleValidationGroup.Pattern.class, DescriptionValidationGroup.NotBlank.class, DescriptionValidationGroup.Pattern.class}) @ModelAttribute NewsPostRequestDto newsPostRequestDto, BindingResult bindingResult, HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // file validations
        if(newsPostRequestDto.getFile().isEmpty()) {
            bindingResult.rejectValue("file","file.empty","Please upload a photo.");
        } else {
            String type = newsPostRequestDto.getFile().getContentType();
            if(!type.equals("image/jpg") && !type.equals("image/jpeg") && !type.equals("image/png")){
                bindingResult.rejectValue("file","file.type","Only JPG, JPEG, or PNG images are allowed.");
            }
            if(newsPostRequestDto.getFile().getSize() > 2*1024*1024){
                bindingResult.rejectValue("file","file.size","Maximum allowed size is 2 MB.");
            }
        }

        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fe -> {
                errors.put(fe.getField(), fe.getDefaultMessage());
            });
            result.put("status", "error");
            result.put("errors", errors);
            return result;
        }

        // save news
        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");
        newsService.saveNewsPost(userProfileDto, newsPostRequestDto);

        result.put("status", "success");
        return result;
    }

    @DeleteMapping("/delete/{newsId}")
    @ResponseBody
    public String deleteNews(@PathVariable("newsId") Long newsId){
        newsService.deleteNews(newsId);
        return "success";
    }

    @GetMapping("/home")
    @ResponseBody
    public List<NewsListDto> getHomeNews(@RequestParam(required = false) Long categoryId){
        if(categoryId != null){
            return newsService.getCategoryNews(categoryId,200);
        }
        return newsService.getLatestNewsForHome(200);
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public List<NewsListDto> getUserNews(@PathVariable("userId") Long userId){
        return newsService.getUserPostNews(userId);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<NewsListDto> searchNews(@RequestParam("q") String keyword){
        return newsService.searchNews(keyword);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNews() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        newsService.addEmitter(emitter);
        return emitter;
    }

    @GetMapping("/details/{newsId}")
    public String viewNewsDetails(@PathVariable Long newsId, Model model){
        NewsListDto newsListDto = newsService.getNewsDetails(newsId);
        model.addAttribute("newsDetails", newsListDto);
        return "user/news-details";
    }

    @PostMapping("/subscribe")
    @ResponseBody
    public Map<String, Object> subscribeUser(HttpSession session){

        Map<String, Object> response = new HashMap<>();
        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if(userProfileDto == null) {
            response.put("success", false);
            response.put("redirect", "/auth/sign-in");
            return response;
        }

        newsService.subscribeUser(userProfileDto.getEmail());
        userProfileDto.setSubscribed(true);

        response.put("success", true);
        response.put("subscribed", true);
        return response;
    }

    @PostMapping("/unsubscribe")
    @ResponseBody
    public Map<String, Object> unsubscribeUser(HttpSession session){

        Map<String, Object> response = new HashMap<>();
        UserProfileDto userProfileDto = (UserProfileDto) session.getAttribute("loggedInUser");

        if(userProfileDto == null) {
            response.put("success", false);
            response.put("redirect", "/auth/sign-in");
            return response;
        }

        newsService.unsubscribeUser(userProfileDto.getEmail());
        userProfileDto.setSubscribed(false);

        response.put("success", true);
        response.put("subscribed", false);
        return response;
    }
}
