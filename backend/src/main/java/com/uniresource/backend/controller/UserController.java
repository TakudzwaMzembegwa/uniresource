package com.uniresource.backend.controller;

import java.io.IOException;
import java.util.List;

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
		headers.setAccessControlAllowMethods(List.of(HttpMethod.POST));
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		return ResponseEntity.ok()
				.headers(headers)
				.body(EntityModel.of(new SigninForm(), 
					linkTo(methodOn(UserController.class).signin()).withSelfRel()
						.andAffordance(afford(methodOn(UserController.class).signup(null, new CreateUserRequest()))),
					linkTo(methodOn(RootController.class).root(null)).withRel("home")
				));
}

	@GetMapping("/signup")
	public ResponseEntity<EntityModel<CreateUserRequest>> signup() {
	
		return ResponseEntity
				.ok(EntityModel.of(new CreateUserRequest(),
					linkTo(methodOn(UserController.class).signup()).withSelfRel(),
					linkTo(methodOn(UserController.class).signin()).withRel("signin"),
					linkTo(methodOn(RootController.class).root(null)).withRel("home")
				));
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
					linkTo(methodOn(RootController.class).root(null)).withRel("home"),
					linkTo(UserController.class).slash("logout").withRel("logout")
				));
	}

	@PostMapping(path = "/update", consumes = "multipart/form-data")
	public ResponseEntity<EntityModel<UserDto>> updateUser(@RequestPart("file") @Valid MultipartFile file
			, Authentication authentication, @RequestPart("request") @Valid @NotNull UpdateUserRequest request) {
 
		return ResponseEntity
				.ok(EntityModel.of(userService.update(file, request, authentication.getName()),
					linkTo(methodOn(UserController.class).updateUser(null, null, request)).withSelfRel(),
					linkTo(methodOn(UserController.class).getUser(null, authentication.getName())).withRel("userDetails"),
					linkTo(methodOn(UserController.class).updatePassword(null)).withRel("updatePassword"),
					linkTo(methodOn(UserController.class).deleteUser(null)).withRel("deleteUser"),
					linkTo(UserController.class).slash("logout").withRel("logout")
				));
	}

	@GetMapping("/change_password")
	public ResponseEntity<EntityModel<UpdatePasswordRequest>> updatePassword(Authentication authentication) {

		return ResponseEntity
				.ok(EntityModel.of(new UpdatePasswordRequest(),
					linkTo(methodOn(UserController.class).updatePassword(null)).withSelfRel(),
					linkTo(methodOn(UserController.class).getUser(null, authentication.getName())).withRel("userDetails"),
					linkTo(methodOn(UserController.class).updateUser(null)).withRel("updateUser"),
					linkTo(methodOn(UserController.class).deleteUser(null)).withRel("deleteUser"),
					linkTo(UserController.class).slash("logout").withRel("logout")
				));
	}

	/*@PatchMapping("/change_password")
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
	}*/

	@PostMapping("/change_password")
	public ResponseEntity<?> updatePassword(HttpServletRequest request, HttpServletResponse response, Authentication authentication,
			@RequestBody UpdatePasswordRequest updatePasswordRequest) {

		new SecurityContextLogoutHandler().logout(request, response, authentication);
		try {
			new UserLogoutSuccessHandler().onLogoutSuccess(request, response, authentication);
			userService.updatePassword(updatePasswordRequest, authentication.getName());
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/*@PostMapping("/deleter")
	public ResponseEntity<EntityModel<DeleteUserRequest>> deleteUser(Authentication authentication, String username) {
		EntityModel<DeleteUserRequest> model = EntityModel.of(new DeleteUserRequest());
		model.add(linkTo(methodOn(UserController.class).deleteUser(authentication)).withSelfRel());
		model.add(linkTo(methodOn(UserController.class).getUser(authentication, authentication.getName()))
				.withRel("userDetails"));
		model.add(linkTo(methodOn(UserController.class).updateUser(authentication)).withRel("updateUser"));
		model.add(linkTo(methodOn(UserController.class).updatePassword(authentication)).withRel("updatePassword"));
		model.add(linkTo(UserController.class).slash("logout").withRel("logout"));
		return ResponseEntity.ok(model);
	}*/

	@GetMapping("/delete")
	public ResponseEntity<EntityModel<DeleteUserRequest>> deleteUser(Authentication authentication) {
		
		return ResponseEntity
				.ok(EntityModel.of(new DeleteUserRequest(),
					linkTo(methodOn(UserController.class).deleteUser(null)).withSelfRel(),
					linkTo(methodOn(UserController.class).getUser(null, authentication.getName())).withRel("userDetails"),
					linkTo(methodOn(UserController.class).updateUser(null)).withRel("updateUser"),
					linkTo(methodOn(UserController.class).updatePassword(null)).withRel("updatePassword"),
					linkTo(UserController.class).slash("logout").withRel("logout")
				));
	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication,
			@RequestBody DeleteUserRequest deleteUserRequest) {

		new SecurityContextLogoutHandler().logout(request, response, authentication);
		try {
			new UserLogoutSuccessHandler().onLogoutSuccess(request, response, authentication);
			userService.delete(authentication.getName(), deleteUserRequest.getPassword());
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@GetMapping("/user/{username}")
	public ResponseEntity<EntityModel<UserDto>> getUser(Authentication authentication,
			@PathVariable("username") String username) {

		return repo.findByUsername(username).map(user -> EntityModel.of(userMapper.toUserDto(user),
				linkTo(methodOn(UserController.class).getUser(authentication, user.getUsername())).withSelfRel()
						.andAffordance(
								afford(methodOn(UserController.class).updateUser(null, null, new UpdateUserRequest())))
						.andAffordance(afford(
								methodOn(UserController.class).updatePassword(null, null, null, new UpdatePasswordRequest()))),
				linkTo(UserController.class).slash("logout").withRel("logout"))).map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

}
