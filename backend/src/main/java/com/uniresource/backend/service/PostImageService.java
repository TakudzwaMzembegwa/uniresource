package com.uniresource.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import com.uniresource.backend.domain.dto.UpdatePostImage;
import com.uniresource.backend.domain.entity.Post;
import com.uniresource.backend.domain.entity.PostImage;
import com.uniresource.backend.domain.mapper.PostImageMapper;
import com.uniresource.backend.repository.PostImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostImageService {

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PostImageMapper postImageMapper;

    public PostImage save(MultipartFile file, PostImage postImage) {
        if (!postImage.getImage().isBlank() && file.getContentType() != null) {
            postImage.setImage(fileStorageService.storeFile(file, PostImage.PREFIX,
                    postImage.getImage().substring(0, postImage.getImage().lastIndexOf(".") + 1)
                            + file.getContentType().substring(6)));
        } else
            postImage.setImage(fileStorageService.storeFile(file, PostImage.PREFIX, postImage.getImage()));
        return postImageRepository.save(postImage);
    }

    public List<PostImage> save(MultipartFile[] files, List<UpdatePostImage> updatePostImages, Post post) {
        List<PostImage> postImages = postImageMapper.toPostImage(updatePostImages.stream().filter(p -> p.getImage() != null).collect(Collectors.toList()));
        PostImage postImage;
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            if (updatePostImages.get(i).getImage() == null) {
                if (counter < files.length) {
                    postImage = this.create(files[counter++]);
                    postImage.setPost(post);
                    postImages.add(postImageRepository.save(postImage));
                } else {
                    break;
                }
            } else if (updatePostImages.get(i).getNewImageName() != null) {
                postImage = postImageMapper.toPostImage(updatePostImages.get(i));
                postImage.setPost(post);
                postImages.add(i, save(files[counter++], postImage));
            }
        }
        return postImages;
    }

    public PostImage create(MultipartFile file) {
        PostImage postImage = new PostImage();
        postImage.setImage(fileStorageService.storeFile(file, PostImage.PREFIX, ""));
        return postImage;
    }

    public void delete(PostImage postImage) {
        fileStorageService.delete(postImage.getImage());
        postImageRepository.delete(postImage);
    }
}
