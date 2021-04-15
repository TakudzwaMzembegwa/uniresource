package com.uniresource.backend.service;

import java.time.Instant;

import javax.validation.ValidationException;

import com.uniresource.backend.domain.dto.CreateUserRequest;
import com.uniresource.backend.domain.dto.UpdatePasswordRequest;
import com.uniresource.backend.domain.dto.UpdateUserRequest;
import com.uniresource.backend.domain.dto.UserDto;
import com.uniresource.backend.domain.entity.User;
import com.uniresource.backend.domain.mapper.UserMapper;
import com.uniresource.backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserDto save(MultipartFile file, CreateUserRequest request) {
        if (userExists(request.getEmail()) || userExists(request.getUsername())) {
            throw new ValidationException("Username or email already exists");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Password does not match rePassword");
        }

        User user = userMapper.createUser(request);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        if (file.isEmpty())
            return userMapper.toUserDto(userRepository.save(user));
        user.setProfilePic(fileStorageService.storeFile(file, User.PREFIX, ""));
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto update(MultipartFile file, UpdateUserRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        user = userMapper.updateUser(request, user);
        if (file.isEmpty())
            return userMapper.toUserDto(userRepository.save(user));
        user.setProfilePic(fileStorageService.storeFile(file, User.PREFIX, user.getProfilePic()));
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto updatePassword(UpdatePasswordRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        if (request.getNewPassword().equals(request.getConfirmNewPassword())) {
            if (bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
                return userMapper.toUserDto(userRepository.save(user));
            }
            throw new ValidationException("Wrong credintials");
        }
        throw new ValidationException("Passwords do not match");
    }

    @Transactional
    public User getUser(String username) {
        User user;
        if (username.contains(".")) {
            user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        } else
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return user;
    }

    @Transactional
    public UserDto delete(String username, String password) {
        User user = getUser(username);
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            userRepository.delete(user);
            fileStorageService.delete(user.getProfilePic());
            return userMapper.toUserDto(user);
        }
        throw new ValidationException("Wrong password");
    }

    public boolean userExists(String username) {
        if (username.contains("."))
            return userRepository.existsByEmail(username);
        else
            return userRepository.existsByUsername(username);
    }

}