package com.uniresource.backend.domain.mapper;

import java.util.List;

import com.uniresource.backend.domain.dto.CreatePostRequest;
import com.uniresource.backend.domain.dto.PostDto;
import com.uniresource.backend.domain.dto.PostSummary;
import com.uniresource.backend.domain.dto.UpdatePostImage;
import com.uniresource.backend.domain.dto.UpdatePostRequest;
import com.uniresource.backend.domain.entity.Post;
import com.uniresource.backend.domain.entity.PostES;
import com.uniresource.backend.domain.entity.PostImage;

import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { UserMapper.class, LocationMapper.class, PostImageMapper.class })
public abstract class PostMapper {

    @Mapping(target = "dateCreated", dateFormat = "dd/MMM/yyyy")
    public abstract PostDto toPostDto(Post post);

    @InheritInverseConfiguration
    public abstract Post toPost(PostDto postDto);

    public abstract Post createPost(CreatePostRequest request);

    // for putting post id on image
    @AfterMapping
    protected Post afterToPost(PostDto postDto, @MappingTarget Post post) {
        for (PostImage postImage : post.getPostImages()) {
            postImage.setPost(post);
        }
        return post;
    }

    /*@Mapping(target = "dateCreated", dateFormat = "dd/MMM/yyyy")
    @Mapping(target = "postImage", expression = "java(  post.getPostImages().get(1).getImage() )")
    //@Mapping(target = "postImage", expression = "java( new PostImageDto(post.getPostImages().get(1).getId(), post.getPostImages().get(1).getImage()) )")
   // @Mapping(target = "totalImages", expression = "java( post.getPostImages().size() )")
    public abstract PostSummary toPostSummary(Post post);

    public abstract List<PostSummary> toPostSummary(List<Post> posts);*/

    @Mapping(target = "dateCreated", dateFormat = "dd/MMM/yyyy")
    public abstract PostSummary toPostSummary(PostES post);

    public abstract List<PostSummary> toPostSummary(List<PostES> posts);

    @Mapping(target = "postId", source = "id")
    public abstract UpdatePostRequest toUpdatePostRequest(Post post);

    @AfterMapping
    protected UpdatePostRequest afterToUpdatePostRequest(Post post, @MappingTarget UpdatePostRequest updatePostRequest) {
        for (int i =  updatePostRequest.getPostImages().size(); i < 6; i++) {
            updatePostRequest.getPostImages().add(i, new UpdatePostImage());
        }
        return updatePostRequest;
    }
}