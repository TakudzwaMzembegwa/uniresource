package com.uniresource.backend.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.uniresource.backend.domain.dto.CountrySelector;
import com.uniresource.backend.domain.dto.CreatePostRequest;
import com.uniresource.backend.domain.dto.PostDto;
import com.uniresource.backend.domain.dto.PostSearchRequest;
import com.uniresource.backend.domain.dto.PostSummary;
import com.uniresource.backend.domain.dto.UpdatePostImage;
import com.uniresource.backend.domain.dto.UpdatePostRequest;
import com.uniresource.backend.domain.entity.Location;
import com.uniresource.backend.domain.entity.Post;
import com.uniresource.backend.domain.mapper.LocationMapper;
import com.uniresource.backend.domain.mapper.PostImageMapper;
import com.uniresource.backend.domain.mapper.PostMapper;
import com.uniresource.backend.repository.LocationRepository;
import com.uniresource.backend.repository.PostImageRepository;
import com.uniresource.backend.repository.PostRepository;
import com.uniresource.backend.service.PostService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    public static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    PostImageRepository postImageRepository;
    @Autowired
    LocationMapper locationMapper;
    @Autowired
    PostImageMapper postImageMapper;
    @Autowired
    PostMapper postMapper;

    @GetMapping("search")
    public List<PostSummary> search(@RequestBody PostSearchRequest postSearch) {
        return postService.search(postSearch);
    }

    @GetMapping("search_params")
    public PostSearchRequest postSearchRequest() {
        return new PostSearchRequest();
    }

    @GetMapping("create")
    public CreatePostRequest createPostRequest() {
        return new CreatePostRequest();
    }

    @PostMapping("create")
    public PostDto createPost(@RequestPart("files") @Valid MultipartFile[] files,
            @RequestPart("request") @Valid @NotNull CreatePostRequest request, Authentication authentication) {
        return postService.save(files, request, authentication.getName());
    }

    @GetMapping("update/{id}")
    public UpdatePostRequest updatePost(@PathVariable long id) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(postRepository.findById(id).orElseThrow());
        updatePostRequest.getPostImages().stream().forEach(UpdatePostImage::makeURI);
        return updatePostRequest;
    }

    @PatchMapping("update")
    public UpdatePostRequest updatePost(@RequestPart("files") @Valid MultipartFile[] files,
            @RequestPart("request") @Valid @NotNull UpdatePostRequest updatePostRequest) throws IOException {
        postService.update(files, updatePostRequest);
        //return user to posts
        return updatePostRequest;
    }

    @GetMapping("p/{id}")
    public PostDto getPost(@PathVariable long id) {
        return postService.getPostDto(id);
    }

    @DeleteMapping("delete/{id}")
    public void deleteMapping(@PathVariable long id, Authentication authentication) {
        postService.delete(id);
    }
}
