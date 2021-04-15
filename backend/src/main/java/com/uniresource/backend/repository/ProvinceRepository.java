package com.uniresource.backend.repository;

import com.uniresource.backend.domain.entity.Province;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    
    public Province findByName(String name);
}
