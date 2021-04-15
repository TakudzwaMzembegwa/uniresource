package com.uniresource.backend.repository;

import com.uniresource.backend.domain.entity.University;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
    
    public University findByName(String name);
}
