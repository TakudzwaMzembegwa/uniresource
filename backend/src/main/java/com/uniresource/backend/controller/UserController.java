package com.uniresource.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.uniresource.backend.domain.dto.CreateUserRequest;
import com.uniresource.backend.domain.dto.DeleteUserRequest;
import com.uniresource.backend.domain.dto.SigninForm;
import com.uniresource.backend.domain.dto.UpdatePasswordRequest;
import com.uniresource.backend.domain.dto.UpdateUserRequest;
import com.uniresource.backend.domain.dto.UserDto;
import com.uniresource.backend.domain.entity.User;
import com.uniresource.backend.domain.mapper.UserMapper;
import com.uniresource.backend.repository.UserRepository;
import com.uniresource.backend.security.handler.UserLogoutSuccessHandler;
import com.uniresource.backend.service.UserService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.AffordanceModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.QueryParameter;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRepository repo;

	@GetMapping("/signin")
	public ResponseEntity<EntityModel<SigninForm>> signin(){
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(linkTo(UserController.class).slash("login").withRel("login").toUri());
		headers.setAccessControlAllowMethods(Arrays.asList(new HttpMethod[]{HttpMethod.POST}));
		headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
		return ResponseEntity.ok()
				.headers(headers)
				.body(EntityModel.of(new SigninForm(), 
					linkTo(methodOn(UserController.class).signin()).withSelfRel()
						.andAffordance(afford(methodOn(UserController.class).signup(null, new CreateUserRequest()))),
					linkTo(methodOn(RootController.class).root()).withRel("home")
				));
}

	@GetMapping("/signup")
	public ResponseEntity<EntityModel<CreateUserRequest>> signup() {
		EntityModel<CreateUserRequest> model = EntityModel.of(new CreateUserRequest());
		model.add(linkTo(methodOn(UserController.class).signup()).withSelfRel());
		model.add(linkTo(methodOn(UserController.class).signin()).withRel("signin"));
		model.add(linkTo(methodOn(RootController.class).root()).withRel("home"));
		return ResponseEntity.ok(model);
	}

	@PostMapping(path = "/signup", consumes = "multipart/form-data")
	public ResponseEntity<?> signup(@RequestPart("file") @Valid MultipartFile file,
			@RequestPart("request") @Valid @NotNull CreateUserRequest request) {

		userService.save(file, request);
		return ResponseEntity
						.created(linkTo(UserController.class).slash("login").withRel("login").toUri())
						.build();
	}

	@GetMapping("/update")
	public ResponseEntity<EntityModel<UpdateUserRequest>> updateUser(Authentication authentication) {
		
		return ResponseEntity
				.ok(EntityModel.of(new UpdateUserRequest(),
					linkTo(methodOn(UserController.class).updateUser(null)).withSelfRel(),
					linkTo(methodOn(UserController.class).getUser(null, authentication.getName())).withRel("userDetails"),
					linkTo(methodOn(UserController.class).updatePassword(null)).withRel("changePassword"),
					linkTo(methodOn(UserController.class).deleteUser(null)).withRel("deleteUser"),
					linkTo(methodOn(RootController.class).root()).withRel("home"),
					linkTo(UserController.class).slash("logout").withRel("logout")
				));
	}

	@PostMapping(path = "/update", consumes = "multipart/form-data")
	public ResponseEntity<EntityModel<UserDto>> updateUser(@RequestPart("file") @Valid MultipartFile file,
			@RequestPart("request") @Valid @NotNull UpdateUserRequest request,
			Authentication authentication) {

		EntityModel<UserDto> model = EntityModel.of(userService.update(file, request, authentication.getName()));
		model.add(linkTo(methodOn(UserController.class).updateUser(file, request, authentication)).withSelfRel());
		model.add(linkTo(methodOn(UserController.class).getUser(authentication, authentication.getName()))
				.withRel("userDetails"));
		model.add(linkTo(methodOn(UserController.class).updatePassword(authentication)).withRel("updatePassword"));
		model.add(linkTo(methodOn(UserController.class).deleteUser(authentication)).withRel("deleteUser"));
		model.add(linkTo(UserController.class).slash("logout").withRel("logout"));
		return ResponseEntity.ok(model);
	}

	@GetMapping("/change_password")
	public ResponseEntity<EntityModel<UpdatePasswordRequest>> updatePassword(Authentication authentication) {

		EntityModel<UpdatePasswordRequest> model = EntityModel.of(new UpdatePasswordRequest());
		model.add(linkTo(methodOn(UserController.class).updatePassword(authentication)).withSelfRel());
		model.add(linkTo(methodOn(UserController.class).getUser(authentication, authentication.getName()))
				.withRel("userDetails"));
		model.add(linkTo(methodOn(UserController.class).updateUser(authentication)).withRel("updateUser"));
		model.add(linkTo(methodOn(UserController.class).deleteUser(authentication)).withRel("deleteUser"));
		model.add(linkTo(UserController.class).slash("logout").withRel("logout"));
		return ResponseEntity.ok(model);
	}

	@PatchMapping("/change_password")
	public ResponseEntity<EntityModel<UpdatePasswordRequest>> updatePassword(Authentication authentication,
			@RequestBody UpdatePasswordRequest updatePasswordRequest) {

		EntityModel<UpdatePasswordRequest> model = EntityModel.of(new UpdatePasswordRequest());
		model.add(linkTo(methodOn(UserController.class).updatePassword(authentication)).withSelfRel());
		model.add(linkTo(methodOn(UserController.class).getUser(authentication, authentication.getName()))
				.withRel("userDetails"));
		model.add(linkTo(methodOn(UserController.class).updateUser(authentication)).withRel("updateUser"));
		model.add(linkTo(methodOn(UserController.class).deleteUser(authentication)).withRel("deleteUser"));
		model.add(linkTo(UserController.class).slash("logout").withRel("logout"));
		return ResponseEntity.ok(model);
	}

	@PostMapping("/change_password")
	public void updatePassword(HttpServletRequest request, HttpServletResponse response, Authentication authentication,
			@RequestBody UpdatePasswordRequest updatePasswordRequest) {

		userService.updatePassword(updatePasswordRequest, authentication.getName());
		new SecurityContextLogoutHandler().logout(request, response, authentication);
		try {
			new UserLogoutSuccessHandler().onLogoutSuccess(request, response, authentication);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/deleter")
	public ResponseEntity<EntityModel<DeleteUserRequest>> deleteUser(Authentication authentication, String username) {
		EntityModel<DeleteUserRequest> model = EntityModel.of(new DeleteUserRequest());
		model.add(linkTo(methodOn(UserController.class).deleteUser(authentication)).withSelfRel());
		model.add(linkTo(methodOn(UserController.class).getUser(authentication, authentication.getName()))
				.withRel("userDetails"));
		model.add(linkTo(methodOn(UserController.class).updateUser(authentication)).withRel("updateUser"));
		model.add(linkTo(methodOn(UserController.class).updatePassword(authentication)).withRel("updatePassword"));
		model.add(linkTo(UserController.class).slash("logout").withRel("logout"));
		return ResponseEntity.ok(model);
	}

	@GetMapping("/delete")
	public ResponseEntity<EntityModel<DeleteUserRequest>> deleteUser(Authentication authentication) {
		EntityModel<DeleteUserRequest> model = EntityModel.of(new DeleteUserRequest());
		model.add(linkTo(methodOn(UserController.class).deleteUser(authentication)).withSelfRel());
		model.add(linkTo(methodOn(UserController.class).getUser(authentication, authentication.getName()))
				.withRel("userDetails"));
		model.add(linkTo(methodOn(UserController.class).updateUser(authentication)).withRel("updateUser"));
		model.add(linkTo(methodOn(UserController.class).updatePassword(authentication)).withRel("updatePassword"));
		model.add(linkTo(UserController.class).slash("logout").withRel("logout"));
		return ResponseEntity.ok(model);
	}

	@PostMapping("/delete")
	public void deleteUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication,
			@RequestBody DeleteUserRequest deleteUserRequest) {

		userService.delete(authentication.getName(), deleteUserRequest.getPassword());
		new SecurityContextLogoutHandler().logout(request, response, authentication);
		try {
			new UserLogoutSuccessHandler().onLogoutSuccess(request, response, authentication);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@GetMapping("/user/{username}")
	public ResponseEntity<EntityModel<UserDto>> getUser(Authentication authentication,
			@PathVariable("username") String username) {

		/*
		 * EntityModel<UserDto> model =
		 * EntityModel.of(userMapper.toUserDto(userService.getUser(username)));
		 * model.add(linkTo(methodOn(UserController.class).getUser(authentication,
		 * username)).withSelfRel());
		 * model.add(linkTo(methodOn(UserController.class).deleteUser(authentication)).
		 * withRel("deleteUser"));
		 * model.add(linkTo(methodOn(UserController.class).updateUser(authentication)).
		 * withRel("updateUser"));
		 * model.add(linkTo(methodOn(UserController.class).updatePassword(authentication
		 * )).withRel("updatePassword"));
		 * model.add(linkTo(UserController.class).slash("logout").withRel("logout"));
		 */
		/*
		 * var methodInvocation = methodOn(UserController.class).getUser(authentication,
		 * username);
		 * 
		 * var link = Affordances.of(linkTo(methodInvocation).withSelfRel())
		 * 
		 * .afford(HttpMethod.POST) .withInputAndOutput(UserController.class) //
		 * .withName("createEmployee") //
		 * .withTarget(linkTo(UserController.class).slash("newlink").withRel("linkkkk"))
		 * 
		 * .toLink();
		 */
		return repo.findByUsername(username).map(user -> EntityModel.of(userMapper.toUserDto(user),
				linkTo(methodOn(UserController.class).getUser(authentication, user.getUsername())).withSelfRel()
						.andAffordance(
								afford(methodOn(UserController.class).updateUser(null, new UpdateUserRequest(), null)))
						.andAffordance(afford(
								methodOn(UserController.class).updatePassword(null, new UpdatePasswordRequest()))),
				linkTo(UserController.class).slash("logout").withRel("logout"))).map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

}
