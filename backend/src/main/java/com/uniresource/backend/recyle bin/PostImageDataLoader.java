/*package com.uniresource.backend.post;

import java.io.File;

import com.uniresource.backend.post.Entity.PostImage;
import com.uniresource.backend.post.Repository.PostImageRepository;
import com.uniresource.backend.post.Repository.PostRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("PostDataLoader")
public class PostImageDataLoader {

    public static final Logger log = LoggerFactory.getLogger(PostImageDataLoader.class);

    @Bean("PostImageDataLoader")
    CommandLineRunner initPostImageDataBase(PostImageRepository postImageRepo, PostRepository postRepo) {
        return args -> {
            log.info("Preloading " + postImageRepo.save(new PostImage(
                    new File("C:\\Users\\lenovo\\Pictures\\hashcode.JPG").toURI().toURL(), postRepo.getOne(1L))));
            log.info("Preloading " + postImageRepo.save(new PostImage(
                    new File("C:\\Users\\lenovo\\Pictures\\hyena with heart.JPG").toURI().toURL(), postRepo.getOne(1L))));
        };
    }
}*/
