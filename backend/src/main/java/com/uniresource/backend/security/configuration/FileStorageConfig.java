package com.uniresource.backend.security.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "file")
public class FileStorageConfig {

    private String fileName;

    private String uploadDir;
}
