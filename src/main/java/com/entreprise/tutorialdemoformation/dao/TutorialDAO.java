package com.entreprise.tutorialdemoformation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entreprise.tutorialdemoformation.models.Tutorial;

@Repository
public interface TutorialDAO extends JpaRepository<Tutorial, Long> {
    
}
