package com.uniresource.backend.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String fileName;

    private String uploadDir;

    public static final String IMAGEROOT = "http://localhost:8080/image/";
}
