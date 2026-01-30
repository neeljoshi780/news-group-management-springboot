package com.newsgroupmanagement.service;

import com.newsgroupmanagement.dto.NewsListDto;
import com.newsgroupmanagement.dto.NewsPostRequestDto;
import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.model.Category;
import com.newsgroupmanagement.model.News;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.CategoryRepository;
import com.newsgroupmanagement.repository.NewsRepository;
import com.newsgroupmanagement.repository.UserRepository;
import com.newsgroupmanagement.util.FileOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NewsService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final FileOperation fileOperation;
    private final NotificationService notificationService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @Autowired
    public NewsService(UserRepository userRepository, NewsRepository newsRepository, CategoryRepository categoryRepository, NotificationService notificationService, FileOperation fileOperation) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.fileOperation = fileOperation;
        this.notificationService = notificationService;
    }

    public void saveNewsPost(UserProfileDto userProfileDto, NewsPostRequestDto newsPostRequestDto) {

        String email = userProfileDto.getEmail();
        User user = userRepository.findByEmail(email).isPresent() ? userRepository.findByEmail(email).get() : null;

        if(user != null){

            // News Photo save
            String newsPhotoName = fileOperation.newsPhotoUpload(newsPostRequestDto.getFile());

            // News Save
            News news = new News();
            Category category = categoryRepository.findById(Long.parseLong(newsPostRequestDto.getCategoryId())).orElse(null);
            news.setCategory(category);
            news.setTitle(newsPostRequestDto.getTitle());
            news.setDescription(newsPostRequestDto.getDescription());
            news.setImageUrl(newsPhotoName);
            news.setUser(user);
            newsRepository.save(news);
            broadcastNewNews();
            notificationService.notifySubscribedUsersByEmail(news);
        }
    }

    public void deleteNews(Long id){
        fileOperation.deleteNewsPhoto(newsRepository.findByNewsImageUrl(id));
        newsRepository.deleteById(id);
    }

    public List<NewsListDto> getLatestNewsForHome(int limit) {
        return newsRepository.findByLatestNews(limit);
    }

    public List<NewsListDto> getCategoryNews(Long categoryId, int limit) {
        return newsRepository.findByCategoryIdNews(categoryId, limit);
    }

    public List<NewsListDto> getUserPostNews(Long id){
        return newsRepository.findByUserPostNews(id);
    }

    public List<NewsListDto> searchNews(String keyword){
        return newsRepository.searchNewsByKeyword("%"+keyword+"%");
    }

    public void addEmitter(SseEmitter emitter){
        emitters.add(emitter);

        // Clean up on completion, timeout, or error
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
    }

    @Async
    public void broadcastNewNews(){
        for(SseEmitter emitter : emitters){
            try{
                emitter.send(SseEmitter.event().name("new").data("new-news-created"));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }

    public NewsListDto getNewsDetails(Long newsId){
        return newsRepository.findNewsDetailsById(newsId);
    }

    public void subscribeUser(String email){

        User user = userRepository.findByEmail(email).orElse(null);

        if(user != null){
            user.setSubscribed(true);
            userRepository.save(user);
        }
    }

    public void unsubscribeUser(String email){

        User user = userRepository.findByEmail(email).orElse(null);

        if(user != null){
            user.setSubscribed(false);
            userRepository.save(user);
        }
    }
}
