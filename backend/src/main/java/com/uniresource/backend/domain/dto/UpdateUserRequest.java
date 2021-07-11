package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
 
	private String  firstname;

	private String lastname;

    private String phoneNumber;
    
	private String about;

	private String profilePic;

	private LocationDto location;

	private String gender;

	private String studyYear;
}
