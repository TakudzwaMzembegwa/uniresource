package com.uniresource.backend.domain.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    
    private String username;

	private String firstname;

	private String lastname;

	private String email;

    private String phoneNumber;
    
	private String password;
	
	private String confirmPassword;

	private String about;

	private LocationDto location;

	private String gender;

	private String studyYear;

}
