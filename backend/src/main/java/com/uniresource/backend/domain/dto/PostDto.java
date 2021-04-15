package com.uniresource.backend.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDto {
    private long id;
    @NotBlank
    private String title;
    private String description;
    private float price;
    private String dateCreated;
    @NotNull
    private LocationDto location;
    @NotNull
    private UserDto user;
    private List<PostImageDto> postImages = new ArrayList<>();
    @NotBlank
    private String category;
    @NotBlank
    private String condition;
   // public PostStatus postStatus; check(in service) before sending to client
   

    
}