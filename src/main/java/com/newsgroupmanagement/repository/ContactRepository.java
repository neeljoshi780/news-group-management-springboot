package com.newsgroupmanagement.repository;

import com.newsgroupmanagement.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {

    @Query(value = """
SELECT *
FROM contact c
WHERE CAST(c.id AS TEXT) ILIKE ?1
   OR c.email ILIKE ?1
   OR c.full_name ILIKE ?1
   OR c.message ILIKE ?1
   OR c.phone ILIKE ?1
ORDER BY c.id DESC
LIMIT 300
""", nativeQuery = true)
    List<Contact> searchByContacts(String searchPattern);
}
