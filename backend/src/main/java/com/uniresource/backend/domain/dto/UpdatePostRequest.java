package com.uniresource.backend.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePostRequest {
    @NotNull
    private long postId;
    private String title = "";
    private String description = "";
    private float price = 0;
    private SimpleLocation location;
    private List<UpdatePostImage> postImages = new ArrayList<>(6);   
    private String category = "";
    private String condition = "";
   //private PostStatus postStatus; check(in service) before sending to client
   
}
