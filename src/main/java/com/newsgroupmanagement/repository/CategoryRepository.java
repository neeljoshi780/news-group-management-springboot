package com.newsgroupmanagement.repository;

import com.newsgroupmanagement.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query(value="SELECT COUNT(*) > 0 FROM category WHERE name = ?1", nativeQuery = true)
    boolean existsByCategory(String name);

    @Query(value = """
SELECT
    c.category_id,
    c.created_at,
    c.description,
    c.name
FROM category c
WHERE
    CAST(c.category_id AS TEXT) ILIKE ?1
    OR CAST(c.created_at AS TEXT) ILIKE ?1
    OR c.description ILIKE ?1
    OR c.name ILIKE ?1
ORDER BY c.created_at ASC
LIMIT 300
""", nativeQuery = true)
    List<Category> searchCategory(String searchPattern);
}