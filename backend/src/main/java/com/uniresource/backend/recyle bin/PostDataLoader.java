/*package com.uniresource.backend.post;

import com.uniresource.backend.location.Repository.LocationRepository;
import com.uniresource.backend.post.Entity.Category;
import com.uniresource.backend.post.Entity.Condition;
import com.uniresource.backend.post.Entity.Post;
import com.uniresource.backend.post.Entity.PostStatus;
import com.uniresource.backend.post.Repository.PostRepository;
import com.uniresource.backend.user.Repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("UserDataLoader")
public class PostDataLoader {
        public static final Logger log = LoggerFactory.getLogger(PostDataLoader.class);

        @Bean("PostDataLoader")
        CommandLineRunner initPostDatabase(PostRepository postRepo, UserRepository userRepo,
                        LocationRepository locationRepo) {
                return args -> {
                        log.info("Preloading " + postRepo.save(new Post("STA112 course guide", "first year stats",
                                        40.00, userRepo.findByEmail("teekay@gmail.com"), locationRepo.getOne(1),
                                        Category.BOOKS, Condition.GOOD, PostStatus.PROCESSING)));
                        log.info("Preloading " + postRepo.save(new Post("Calculus by Stewart",
                                        "first year and second year Calculus textbook", 799.99,
                                        userRepo.findByEmail("sef@gmail.com"), locationRepo.getOne(1), Category.BOOKS,
                                        Condition.MINT, PostStatus.PROCESSING)));
                        log.info("Preloading " + postRepo.save(new Post("Casio Adv calculator",
                                        "advanced calculator for math, stats and anything numbers", 115.00,
                                        userRepo.findByEmail("teekay@gmail.com"), locationRepo.getOne(1),
                                        Category.ACCESSORIES, Condition.FAIR, PostStatus.PROCESSING)));
                };
        }
}
*/