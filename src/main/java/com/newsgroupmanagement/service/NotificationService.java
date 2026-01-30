package com.newsgroupmanagement.service;

import com.newsgroupmanagement.model.News;
import com.newsgroupmanagement.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public NotificationService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Async
    public void notifySubscribedUsersByEmail(News news){

        String newsPostedUserEmail = news.getUser().getEmail();
        String postedBy = news.getUser().getFirstName()+" "+news.getUser().getLastName();
        String categoryName = news.getCategory().getName();
        String newsTitle = news.getTitle();
        if (newsTitle.length() > 100) {
            newsTitle = newsTitle.substring(0, 100);
        }

        List<String> subscribedEmails = userRepository.findAllBySubscribedTrue();

        for(String email : subscribedEmails){
            if(!newsPostedUserEmail.equals(email)){
                emailService.sendNewsNotificationEmail(email, postedBy, categoryName, newsTitle);
            }
        }
    }
}
