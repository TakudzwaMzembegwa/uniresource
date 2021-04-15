package com.uniresource.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.uniresource.backend.domain.dto.CreateUserRequest;
import com.uniresource.backend.domain.dto.UpdatePasswordRequest;
import com.uniresource.backend.domain.dto.UpdateUserRequest;
import com.uniresource.backend.domain.dto.UserDto;
import com.uniresource.backend.domain.mapper.UserMapper;
import com.uniresource.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.result.view.RedirectView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserMapper userMapper;

	@GetMapping("signup")
	public CreateUserRequest signup() {
		return new CreateUserRequest();
	}

	@PostMapping("signup")
	public RedirectView signup(@RequestPart("file") @Valid MultipartFile file,
			@RequestPart("request") @Valid @NotNull CreateUserRequest request) {
		userService.save(file, request);
		return new RedirectView(ServletUriComponentsBuilder.fromCurrentContextPath().path("login").toUriString(),
				HttpStatus.PERMANENT_REDIRECT);
	}

	@GetMapping("update")
	public UpdateUserRequest updateUser() {
		return new UpdateUserRequest();
	}

	@PostMapping("update")
	public UserDto updateUser(@RequestPart("file") @Valid MultipartFile file,
			@RequestPart("request") @Valid @NotNull UpdateUserRequest request, Authentication authentication) {
		return userService.update(file, request, authentication.getName());
	}
	
	@GetMapping("change_password")
	public UpdatePasswordRequest updatePassword() {
		return new UpdatePasswordRequest();
	}
	
	@PostMapping("change_password")
	public UserDto updatePassword(Authentication authentication, @RequestBody UpdatePasswordRequest request) {
		return userService.updatePassword(request, authentication.getName());
	}

	@GetMapping("delete")
	public Map<String, String> deleteUser() {
		Map<String, String> map = new TreeMap<>();
		map.put("password", null);
		return map;
	}

	@PostMapping("delete")
	public UserDto deleteUser(Authentication authentication, @RequestBody Map<String,Object> body) {
		return userService.delete(authentication.getName(), (String) body.get("password"));
	}

	@GetMapping("user/{username}")
	public UserDto getUser(@PathVariable("username") String username) {
		return userMapper.toUserDto(userService.getUser(username));
	}

}
