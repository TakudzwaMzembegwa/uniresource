package com.uniresource.backend.domain.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;

import com.uniresource.backend.controller.FileController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostImage {

    private Long id;

    private String image;

    private String newImageName;

    public void makeImageURI() {
        if (this.image != null) {
            try {
                this.image = (new URI( linkTo(methodOn(FileController.class).getFile("", null)).withRel("image").getHref() + image.replace(" ", "%20"))).toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
