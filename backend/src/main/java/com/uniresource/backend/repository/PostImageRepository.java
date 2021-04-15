package com.uniresource.backend.repository;

import java.util.List;

import com.uniresource.backend.domain.entity.Post;
import com.uniresource.backend.domain.entity.PostImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long>{
    List<PostImage> findByPost(Post post);
}
