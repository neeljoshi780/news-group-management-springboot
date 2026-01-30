package com.newsgroupmanagement.repository;

import com.newsgroupmanagement.dto.NewsDto;
import com.newsgroupmanagement.dto.NewsListDto;
import com.newsgroupmanagement.dto.RecentNewsDto;
import com.newsgroupmanagement.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Long> {

    @Query(value = """
        SELECT
            n.news_id                    AS news_id,
            n.title                      AS title,
            n.description                AS description,
            n.image_url                  AS image_url,
            c.name                       AS category_name,
            u.first_name                 AS user_first_name,
            u.last_name                  AS user_last_name,
            u.profile_photo_url          AS user_profile_url,
            n.created_at                 AS created_at
        FROM news n
        JOIN category c ON n.category_id = c.category_id
        JOIN users u ON n.user_id = u.id
        WHERE u.enabled = true AND u.is_deleted = false
        ORDER BY n.created_at DESC
        LIMIT ?1
    """, nativeQuery = true)
    List<NewsListDto> findByLatestNews(int limit);

    @Query(value = """
        SELECT
            n.news_id                    AS news_id,
            n.title                      AS title,
            n.description                AS description,
            n.image_url                  AS image_url,
            c.name                       AS category_name,
            u.first_name                 AS user_first_name,
            u.last_name                  AS user_last_name,
            u.profile_photo_url          AS user_profile_url,
            n.created_at                 AS created_at
        FROM news n
        JOIN category c ON n.category_id = c.category_id
        JOIN users u ON n.user_id = u.id
        WHERE u.enabled = true AND u.is_deleted = false
          AND c.category_id = ?1
        ORDER BY n.created_at DESC
        LIMIT ?2
    """, nativeQuery = true)
    List<NewsListDto> findByCategoryIdNews(Long categoryId, int limit);

    @Query(value = """
        SELECT
            n.news_id                    AS news_id,
            n.title                      AS title,
            n.description                AS description,
            n.image_url                  AS image_url,
            c.name                       AS category_name,
            u.first_name                 AS user_first_name,
            u.last_name                  AS user_last_name,
            u.profile_photo_url          AS user_profile_url,
            n.created_at                 AS created_at
        FROM news n
        JOIN category c ON n.category_id = c.category_id
        JOIN users u ON n.user_id = u.id
        WHERE u.id = ?1
        ORDER BY n.created_at DESC
    """, nativeQuery = true)
    List<NewsListDto> findByUserPostNews(Long id);

    @Query(value = """
    SELECT
        n.news_id                    AS news_id,
        n.title                      AS title,
        n.description                AS description,
        n.image_url                  AS image_url,
        c.name                       AS category_name,
        u.first_name                 AS user_first_name,
        u.last_name                  AS user_last_name,
        u.profile_photo_url          AS user_profile_url,
        n.created_at                 AS created_at
    FROM news n
    JOIN category c ON n.category_id = c.category_id
    JOIN users u ON n.user_id = u.id
    WHERE (LOWER(n.title) LIKE LOWER(?1)
       OR LOWER(n.description) LIKE LOWER(?1)
       OR LOWER(u.first_name) LIKE LOWER(?1)
       OR LOWER(u.last_name) LIKE LOWER(?1))
      AND u.enabled = true AND u.is_deleted = false
    ORDER BY n.created_at ASC
    LIMIT 200
""", nativeQuery = true)
    List<NewsListDto> searchNewsByKeyword(String keyword);

    @Query(value = "select image_url from news where news_id = ?1",nativeQuery = true)
    String findByNewsImageUrl(Long newsId);

    @Query(value = """
SELECT
    n.news_id                    AS news_id,
    n.title                      AS title,
    n.description                AS description,
    n.image_url                  AS image_url,
    c.name                       AS category_name,
    u.first_name                 AS user_first_name,
    u.last_name                  AS user_last_name,
    u.profile_photo_url          AS user_profile_url,
    n.created_at                 AS created_at
FROM news n
JOIN category c ON n.category_id = c.category_id
JOIN users u ON n.user_id = u.id
WHERE u.enabled = true
  AND u.is_deleted = false
  AND n.news_id = ?1
""", nativeQuery = true)
    NewsListDto findNewsDetailsById(Long newsId);

    @Query(value = "select count(*) from news", nativeQuery = true)
    Long countByNews();

    @Query(value = """
SELECT  
    u.first_name,
    u.last_name,
    n.title AS news_title,
    n.created_at
FROM news n
JOIN users u ON u.id = n.user_id
ORDER BY n.created_at DESC limit 5
""", nativeQuery = true)
    List<RecentNewsDto> findRecentTop5();

    @Query(value = "SELECT n.news_id AS newsId, n.category_id AS categoryId, n.user_id AS userId, n.title AS title, n.description AS description, n.image_url AS imageUrl, n.created_at AS createdAt FROM news n ORDER BY n.created_at DESC limit 300", nativeQuery = true)
    List<NewsDto> findAllNews();

    @Query(value = """
    SELECT 
        n.news_id AS newsId,
        n.category_id AS categoryId,
        n.user_id AS userId,
        n.title AS title,
        n.description AS description,
        n.image_url AS imageUrl,
        n.created_at AS createdAt
    FROM news n
    WHERE 
        CAST(n.news_id AS TEXT) ILIKE ?1
        OR CAST(n.category_id AS TEXT) ILIKE ?1
        OR CAST(n.user_id AS TEXT) ILIKE ?1
        OR n.title ILIKE ?1
        OR n.description ILIKE ?1
        OR CAST(n.created_at AS TEXT) ILIKE ?1
    ORDER BY n.created_at DESC
    LIMIT 300
    """, nativeQuery = true)
    List<NewsDto> searchNews(String searchPattern);
}