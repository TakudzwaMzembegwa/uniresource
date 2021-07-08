package com.uniresource.backend.controller;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    
    @GetMapping("/")
    public ResponseEntity<RepresentationModel> root(){

        RepresentationModel model = new RepresentationModel();

        model.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).signin()).withRel("signin"));
        model.add(linkTo(methodOn(UserController.class).signup()).withRel("signup"));
        model.add(linkTo(methodOn(PostController.class).createPostRequest()).withRel("createPost"));
        model.add(linkTo(methodOn(PostController.class).postSearchRequest()).withRel("postSearchRequest"));

        return ResponseEntity.ok(model);
    }

}
