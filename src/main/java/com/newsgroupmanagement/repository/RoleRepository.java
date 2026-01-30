package com.newsgroupmanagement.repository;

import com.newsgroupmanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query(value="SELECT COUNT(*) > 0 FROM role WHERE name = ?1", nativeQuery = true)
    boolean existsByRole(String name);

    Optional<Role> findByName(String roleName);
}
