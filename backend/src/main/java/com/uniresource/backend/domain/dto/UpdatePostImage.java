package com.uniresource.backend.domain.dto;

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
    
}
