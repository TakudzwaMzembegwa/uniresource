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
public class UpdatePostImage {

    private Long id;

    private String image;

    private String newImageName;

    public void makeURI() {
        if (this.image != null) {
            try {
                this.image = (new URI(FileStorageUtils.IMAGEROOT + image.replace(" ", "%20"))).toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
