package com.uniresource.backend.controller;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.uniresource.backend.domain.dto.PostSearchRequest;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    
    @GetMapping("/")
    public ResponseEntity<RepresentationModel> root(Authentication authentication){

        return ResponseEntity
                .ok(new RepresentationModel<>().add(
                    linkTo(methodOn(RootController.class).root(null)).withSelfRel()
                    .andAffordance(afford(methodOn(PostController.class).search(new PostSearchRequest())))
                    .andAffordance(afford(methodOn(PostController.class).createPost(null, null, null))),
                    authentication!=null && authentication.isAuthenticated() ?
                        linkTo(UserController.class).slash("logout").withRel("logout") :
                        linkTo(methodOn(UserController.class).signin()).withRel("signin")
                ));
    }

}
