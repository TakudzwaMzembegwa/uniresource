package com.uniresource.backend.service;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;

import com.uniresource.backend.domain.entity.PostImage;
import com.uniresource.backend.security.configuration.FileStorageConfig;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
	
    public static final String THUMBNAIL_PREFIX = "sml";
    @Autowired
    public FileStorageService(FileStorageConfig fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            // throw new FileStorageException("Could not create the directory where the
            // uploaded files will be stored.", ex);
            ex.printStackTrace();
            // throw new Exception("Could not create the directory where the uploaded files
            // will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String prefix, String filename) {
        try {
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                // Copy file to the target location (Replacing existing file with the same name)
                if (filename.isBlank()) {
                    filename = File.createTempFile(prefix, "." + contentType.substring(6),
                            fileStorageLocation.toFile()).getName();
                }
                Path thumbnailPath = this.fileStorageLocation.resolve(FileStorageService.THUMBNAIL_PREFIX + filename);
                BufferedImage thumbnail = Scalr.resize(ImageIO.read(file.getInputStream()), 120);
                Files.copy(file.getInputStream(), this.fileStorageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                ImageIO.write(thumbnail, contentType.substring(6), thumbnailPath.toFile());

                return filename;
            } else {
                // throw new FileStorageException("Sorry! only image content type allowed " +
                // filename);
                throw new Exception("Sorry! only image content type allowed " + filename);
            }

        } catch (Exception e /* IOException ex */) {
            // throw new FileStorageException("Could not store file " + filename + ". Please
            // try again!", ex);
            e.printStackTrace();
            throw new RuntimeException("Could not store file " + filename + ". Please try again!", e);
        }
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                // throw new MyFileNotFoundException("File not found " + filename);
                throw new Exception("File not found " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File not found " + filename, e);
        }
        /*
         * } catch (MalformedURLException ex) { throw new
         * MyFileNotFoundException("File not found " + filename, ex); }
         */
    }

    public void delete(String filename) {
        try {
            Files.delete(this.fileStorageLocation.resolve(filename).normalize());
            Files.delete(this.fileStorageLocation.resolve(THUMBNAIL_PREFIX + filename).normalize());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
