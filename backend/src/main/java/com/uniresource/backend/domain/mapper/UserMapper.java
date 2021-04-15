package com.uniresource.backend.domain.mapper;

import com.uniresource.backend.domain.dto.CreateUserRequest;
import com.uniresource.backend.domain.dto.UpdateUserRequest;
import com.uniresource.backend.domain.dto.UserDto;
import com.uniresource.backend.domain.entity.User;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface UserMapper {

    @Mapping(target = "dateJoined", dateFormat = "dd/MMM/yyyy")
    @Mapping(target = "lastUpdate", dateFormat = "dd/MMM/yyyy")
    public UserDto toUserDto(User user);

    @InheritInverseConfiguration 
    @Mapping(target = "profilePic", expression = "java( user.getProfilePic().substring(user.getProfilePic().lastIndexOf('/') + 1) )")
    public User toUser(UserDto user);

    @Mapping(target = "totalPosts", constant = "0")
    @Mapping(target = "activePosts", constant = "0")
    public User createUser(CreateUserRequest request);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public User updateUser(UpdateUserRequest request, @MappingTarget User user);
}
