package com.uniresource.backend.domain.dto;
 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.constraints.NotBlank;

import com.uniresource.backend.controller.FileController;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

	@NotBlank
	private String username;

	private String  firstname;

	private String lastname;

	@NotBlank
	private String email;

	private String phoneNumber;

	private String dateJoined;

	private String lastUpdate;

	private String about;

	private String profilePic;

	private LocationDto location;

	private String gender;

	private String studyYear;

	private int totalPosts;

	private int activePosts;

	// set total&active posts 0
    
	public void setProfilePic(String profilePic) {
        try {
            this.profilePic = (new URI( linkTo(methodOn(FileController.class).getFile("", null)).withRel("image").getHref() + profilePic.replace(" ", "%20"))).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

	

}
