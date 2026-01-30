package com.newsgroupmanagement.repository;

import com.newsgroupmanagement.dto.UserDto;
import com.newsgroupmanagement.model.Contact;
import com.newsgroupmanagement.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value="SELECT COUNT(*) > 0 FROM users WHERE email = ?1", nativeQuery = true)
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query(value="SELECT is_password_set FROM users WHERE email = ?1", nativeQuery = true)
    boolean isPasswordSetByEmail(String email);

    @Query(value="SELECT email FROM users WHERE subscribed = 'true'", nativeQuery = true)
    List<String> findAllBySubscribedTrue();

    @Query(value = """
SELECT COUNT(DISTINCT u.id) AS total_users
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = false
  AND r.name = 'ROLE_MASTER_ADMIN'
""", nativeQuery = true)
    Long countByMasterAdmin();

    @Query(value = """
SELECT COUNT(DISTINCT u.id) AS total_users
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = false
  AND r.name = 'ROLE_ADMIN'
""", nativeQuery = true)
    Long countByAdmin();

    @Query(value = """
SELECT COUNT(DISTINCT u.id) AS total_users
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = false
  AND r.name = 'ROLE_USER';
""", nativeQuery = true)
    Long countByUser();

    @Query(value = "SELECT u.* FROM users u JOIN user_roles ur ON u.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE u.is_deleted = false AND u.enabled = true AND r.name = 'ROLE_USER' ORDER BY u.id ASC LIMIT 300", nativeQuery = true)
    List<User> findByUserIsDeletedFalseAndEnabledTrue();

    @Query(value = """
SELECT u.*
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = false
  AND u.enabled = true
  AND r.name = 'ROLE_USER'
  AND (
        CAST(u.id AS TEXT) ILIKE ?1
        OR u.first_name ILIKE ?1
        OR u.last_name ILIKE ?1
        OR u.email ILIKE ?1
        OR u.phone ILIKE ?1
      )
ORDER BY u.id ASC
LIMIT 300
""", nativeQuery = true)
    List<User> searchByUser(String searchPattern);

    @Query(value = "SELECT * FROM users WHERE is_deleted=false and enabled = true and id = ?1", nativeQuery = true)
    Optional<User> findActiveUserById(Long id);

    @Query(value = "SELECT u.* FROM users u JOIN user_roles ur ON u.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE u.is_deleted = false AND u.enabled = true AND r.name = 'ROLE_ADMIN' ORDER BY u.id ASC LIMIT 300", nativeQuery = true)
    List<User> findByAdminIsDeletedFalseAndEnabledTrue();

    @Query(value = """
SELECT u.*
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = false
  AND u.enabled = true
  AND r.name = 'ROLE_ADMIN'
  AND (
        CAST(u.id AS TEXT) ILIKE ?1
        OR u.first_name ILIKE ?1
        OR u.last_name ILIKE ?1
        OR u.email ILIKE ?1
        OR u.phone ILIKE ?1
      )
ORDER BY u.id ASC
LIMIT 300
""", nativeQuery = true)
    List<User> searchByAdmins(String searchPattern);

    @Query(value = "SELECT u.* FROM users u JOIN user_roles ur ON u.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE u.is_deleted = false AND u.enabled = false AND r.name = 'ROLE_USER' ORDER BY u.id ASC LIMIT 300", nativeQuery = true)
    List<User> findByUserIsDeletedFalseAndEnabledFalse();

    @Query(value = """
SELECT u.*
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = false
  AND u.enabled = false
  AND r.name = 'ROLE_USER'
  AND (
        CAST(u.id AS TEXT) ILIKE ?1
        OR u.first_name ILIKE ?1
        OR u.last_name ILIKE ?1
        OR u.email ILIKE ?1
        OR u.phone ILIKE ?1
      )
ORDER BY u.id ASC
LIMIT 300
""", nativeQuery = true)
    List<User> searchByBlockUsers(String searchPattern);

    @Query(value = "SELECT u.* FROM users u JOIN user_roles ur ON u.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE u.is_deleted = false AND u.enabled = false AND r.name = 'ROLE_ADMIN' ORDER BY u.id ASC LIMIT 300", nativeQuery = true)
    List<User> findByAdminIsDeletedFalseAndEnabledFalse();

    @Query(value = """
SELECT u.*
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = false
  AND u.enabled = false
  AND r.name = 'ROLE_ADMIN'
  AND (
        CAST(u.id AS TEXT) ILIKE ?1
        OR u.first_name ILIKE ?1
        OR u.last_name ILIKE ?1
        OR u.email ILIKE ?1
        OR u.phone ILIKE ?1
      )
ORDER BY u.id ASC
LIMIT 300
""", nativeQuery = true)
    List<User> searchByBlockAdmins(String searchPattern);

    @Query(value = "SELECT u.* FROM users u JOIN user_roles ur ON u.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE u.is_deleted = true AND r.name = 'ROLE_USER' ORDER BY u.id ASC LIMIT 300", nativeQuery = true)
    List<User> findByUserIsDeletedTrue();

    @Query(value = """
SELECT u.*
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = true
  AND r.name = 'ROLE_USER'
  AND (
        CAST(u.id AS TEXT) ILIKE ?1
        OR u.first_name ILIKE ?1
        OR u.last_name ILIKE ?1
        OR u.email ILIKE ?1
        OR u.phone ILIKE ?1
      )
ORDER BY u.id ASC
LIMIT 300
""", nativeQuery = true)
    List<User> searchByDeletedUsers(String searchPattern);

    @Query(value = "SELECT u.* FROM users u JOIN user_roles ur ON u.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE u.is_deleted = true AND r.name = 'ROLE_ADMIN' ORDER BY u.id ASC LIMIT 300", nativeQuery = true)
    List<User> findByAdminIsDeletedTrue();

    @Query(value = """
SELECT u.*
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
WHERE u.is_deleted = true
  AND r.name = 'ROLE_ADMIN'
  AND (
        CAST(u.id AS TEXT) ILIKE ?1
        OR u.first_name ILIKE ?1
        OR u.last_name ILIKE ?1
        OR u.email ILIKE ?1
        OR u.phone ILIKE ?1
      )
ORDER BY u.id ASC
LIMIT 300
""", nativeQuery = true)
    List<User> searchByDeletedAdmins(String searchPattern);
}
