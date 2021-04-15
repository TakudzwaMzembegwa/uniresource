package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
 
	public String fullname;

    public String phoneNumber;
    
	public String about;

	public String profilePic;

	public LocationDto location;

	public String gender;

	public String studyYear;
}
