package com.entreprise.tutorialdemoformation.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entreprise.tutorialdemoformation.models.ERole;
import com.entreprise.tutorialdemoformation.models.Role;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(ERole name);
}
