package com.uniresource.backend.repository;

import com.uniresource.backend.domain.entity.PostES;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostElasticsearchRepository extends ElasticsearchRepository<PostES, String> {

    public void findByPostId(Long postId);

    public void deleteByPostId(Long postId);

}
