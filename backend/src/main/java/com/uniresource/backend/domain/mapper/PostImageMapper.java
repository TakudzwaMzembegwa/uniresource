package com.uniresource.backend.domain.mapper;

import java.util.List;

import com.uniresource.backend.domain.dto.PostImageDto;
import com.uniresource.backend.domain.dto.UpdatePostImage;
import com.uniresource.backend.domain.entity.PostImage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostImageMapper {
    
    public PostImageDto toPostImageDto(PostImage image);

    @Mapping(target = "image", expression = "java( postImageDto.getImage().substring( postImageDto.getImage().lastIndexOf('/')) )")
    public PostImage toPostImage(PostImageDto postImageDto);

    public List<PostImageDto> toPostImageDto(List<PostImage> images);

    public UpdatePostImage toUpdatePostImage(PostImage image);

    public List<UpdatePostImage> toUpdatePostImage(List<PostImage> image);

    public PostImage toPostImage(UpdatePostImage image);

}
