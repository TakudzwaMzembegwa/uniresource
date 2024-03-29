package com.uniresource.backend.domain.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;

import com.uniresource.backend.controller.FileController;
import com.uniresource.backend.domain.entity.Category;
import com.uniresource.backend.domain.entity.Condition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSummary {
    private long postId;
    private String title;
    private String description;
    private float price;
    private String dateCreated;
    private String postImage;
    //private int totalImages; PostMapper toPostSummary(Post post);
    private Category category;
    private Condition condition;

    public void setPostImage(String postImage){
        try {
            this.postImage = (new URI( linkTo(methodOn(FileController.class).getFile("", null)).withRel("image").getHref() + postImage.replace(" ", "%20"))).toString();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
