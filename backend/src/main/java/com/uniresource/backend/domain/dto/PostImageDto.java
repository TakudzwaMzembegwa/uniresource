package com.uniresource.backend.domain.dto;

import java.net.URI;
import java.net.URISyntaxException;

import com.uniresource.backend.security.utils.FileStorageUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostImageDto {

    private Long id;

    private String image;
    
    public void setImage(String image) {
        try {
            this.image = (new URI(FileStorageUtils.IMAGEROOT + image.replace(" ", "%20"))).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
