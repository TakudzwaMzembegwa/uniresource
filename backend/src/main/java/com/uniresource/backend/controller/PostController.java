package com.uniresource.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.uniresource.backend.domain.dto.CreatePostRequest;
import com.uniresource.backend.domain.dto.PostDto;
import com.uniresource.backend.domain.dto.PostSearchRequest;
import com.uniresource.backend.domain.dto.PostSummary;
import com.uniresource.backend.domain.dto.UpdatePostImage;
import com.uniresource.backend.domain.dto.UpdatePostRequest;
import com.uniresource.backend.domain.mapper.LocationMapper;
import com.uniresource.backend.domain.mapper.PostImageMapper;
import com.uniresource.backend.domain.mapper.PostMapper;
import com.uniresource.backend.repository.LocationRepository;
import com.uniresource.backend.repository.PostImageRepository;
import com.uniresource.backend.repository.PostRepository;
import com.uniresource.backend.service.PostService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private final PostService postService;
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final LocationRepository locationRepository;
    @Autowired
    private final PostImageRepository postImageRepository;
    @Autowired
    private final LocationMapper locationMapper;
    @Autowired
    private final PostImageMapper postImageMapper;
    @Autowired
    private final PostMapper postMapper;

    @PostMapping("/search")
    public ResponseEntity<CollectionModel<EntityModel<PostSummary>>> search(@RequestBody PostSearchRequest postSearchRequest) {
        List<EntityModel<PostSummary>> postSummaryResource = postService.search(postSearchRequest).stream()
            .map(post -> EntityModel.of(post, 
                linkTo(methodOn(PostController.class).findPost(post.getPostId(), null)).withSelfRel()
            )).collect(Collectors.toList());
       
        return ResponseEntity.ok(
            CollectionModel.of(postSummaryResource,
                linkTo(methodOn(PostController.class).search(null)).withSelfRel(),
                linkTo(methodOn(PostController.class).createPost(null, null, null)).withRel("createPost"),
                linkTo(methodOn(RootController.class).root(null)).withRel("root")
            ));
    }

    @GetMapping("/search_params")
    public ResponseEntity<EntityModel<PostSearchRequest>> searchParams() {
        return ResponseEntity.ok(
            EntityModel.of(new PostSearchRequest(),
                linkTo(methodOn(PostController.class).searchParams()).withSelfRel()
                    .andAffordance(afford(methodOn(PostController.class).search(new PostSearchRequest()))),
                linkTo(methodOn(RootController.class).root(null)).withRel("root")
            ));
    }

    @GetMapping("/create")
    public ResponseEntity<EntityModel<CreatePostRequest>> createPostRequest() {
        return ResponseEntity.ok(
            EntityModel.of(new CreatePostRequest(),
                linkTo(methodOn(PostController.class).createPostRequest()).withSelfRel()
                    .andAffordance(afford(methodOn(PostController.class).createPost(null, null, null))),
                linkTo(methodOn(PostController.class).search(null)).withRel("search"),
                linkTo(methodOn(RootController.class).root(null)).withRel("root")
            ));
    }

    @PostMapping(path = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(@RequestPart("files") @Valid  MultipartFile[] files,
            @RequestPart("request")  @Valid  /* @NotNull */ CreatePostRequest request, Authentication authentication) {

        try {
			var savedPost = postService.save(files, request, authentication.getName());
			return ResponseEntity //
					.created(linkTo(methodOn(PostController.class).findPost(savedPost.getId(), null)).toUri()) //
					.body(EntityModel.of(savedPost, 
                        linkTo(methodOn(PostController.class).findPost(savedPost.getId(), null)).withSelfRel()
                            .andAffordance(afford(methodOn(PostController.class).updatePost(null, savedPost.getId(), null)))
                            .andAffordance(afford(methodOn(PostController.class).delete(savedPost.getId(), null))),
                        linkTo(methodOn(UserController.class).getUser(null, authentication.getName())).withRel("getUser"),      
                        linkTo(methodOn(PostController.class).search(null)).withRel("search"),
                        linkTo(methodOn(RootController.class).root(null)).withRel("root")
                    ));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Unable to create " + request);
		}  
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<EntityModel<UpdatePostRequest>> updatePost(@PathVariable long id) {
        var updatePostRequest = postMapper.toUpdatePostRequest(postRepository.findById(id).orElseThrow());
        updatePostRequest.getPostImages().stream().forEach(UpdatePostImage::makeImageURI);
        
        return  ResponseEntity.ok(
            EntityModel.of(updatePostRequest,
                linkTo(methodOn(PostController.class).updatePost(id)).withSelfRel()
                    .andAffordance(afford(methodOn(PostController.class).updatePost(null, id, null)))
                    .andAffordance(afford(methodOn(PostController.class).delete(id, null)))
                    .andAffordance(afford(methodOn(PostController.class).findPost(id, null))),    
                linkTo(methodOn(PostController.class).search(null)).withRel("search"),
                linkTo(methodOn(RootController.class).root(null)).withRel("root")
            ));
    }

    @PostMapping(path = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updatePost(@RequestPart("files") @Valid MultipartFile[] files, @PathVariable long id,
            @RequestPart("request") @Valid /* @NotNull */ UpdatePostRequest updatePostRequest) {
        if(id == updatePostRequest.getPostId()){
            try {
                return ResponseEntity.ok(
                    EntityModel.of(postService.update(files, updatePostRequest),
                        linkTo(methodOn(PostController.class).updatePost(null, 0, null)).withSelfRel()
                            .andAffordance(afford(methodOn(PostController.class).updatePost(id)))
                            .andAffordance(afford(methodOn(PostController.class).delete(id, null))),
                        linkTo(methodOn(PostController.class).findPost(id, null)).withRel("findPost"),
                        linkTo(methodOn(PostController.class).search(null)).withRel("search"),
                        linkTo(methodOn(RootController.class).root(null)).withRel("root")
                    ));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> findPost(@PathVariable long id, Authentication authentication) {
        try{
            var postDto = postService.getPostDto(id);
            if(authentication != null && postDto.getUser().getUsername().equals(authentication.getName())){
                return ResponseEntity.ok(
                    EntityModel.of(postDto,
                        linkTo(methodOn(PostController.class).findPost(id, null)).withSelfRel()
                            .andAffordance(afford(methodOn(PostController.class).updatePost(id)))
                            .andAffordance(afford(methodOn(PostController.class).delete(id, null))),
                        linkTo(methodOn(UserController.class).getUser(null, authentication.getName())).withRel("getUser"),  
                        linkTo(methodOn(PostController.class).createPost(null, null, null)).withRel("createPost"),
                        linkTo(methodOn(PostController.class).search(null)).withRel("search"),
                        linkTo(methodOn(RootController.class).root(null)).withRel("root")
                    ));
            }
            else{
                return ResponseEntity.ok(
                    EntityModel.of(postDto,
                        linkTo(methodOn(PostController.class).findPost(id, null)).withSelfRel(),
                        linkTo(methodOn(UserController.class).getUser(null, postDto.getUser().getUsername())).withRel("getUser"), 
                        linkTo(methodOn(PostController.class).createPost(null, null, null)).withRel("createPost"),
                        linkTo(methodOn(PostController.class).search(null)).withRel("search"),
                        linkTo(methodOn(RootController.class).root(null)).withRel("root")  
                    ));
            }
        }
        catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such element found, Id: " + id);
        }
    }

    @PostMapping("/delete/{id}")
    public  ResponseEntity<?> delete(@PathVariable long id, Authentication authentication) {
        if(authentication != null && authentication.getName().equals(postService.getPostDto(id).getUser().getUsername())){
            postService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
