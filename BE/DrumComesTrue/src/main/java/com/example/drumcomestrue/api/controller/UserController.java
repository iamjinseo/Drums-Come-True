package com.example.drumcomestrue.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.drumcomestrue.api.request.user.LoginRequest;
import com.example.drumcomestrue.api.request.user.SignupRequest;
import com.example.drumcomestrue.api.response.user.FindIdResponse;
import com.example.drumcomestrue.api.response.user.FindPwResponse;
import com.example.drumcomestrue.api.response.user.FindResponse;
import com.example.drumcomestrue.api.response.user.LoginResponse;
import com.example.drumcomestrue.api.response.user.ViewResponse;
import com.example.drumcomestrue.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest){
		userService.signup(signupRequest);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login/check/{userId}")
	public ResponseEntity<Void> check(@PathVariable("userId") String userId){
		userService.check(userId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
		userService.login(loginRequest);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@GetMapping("/myPage/{userPk}")
	public ResponseEntity<ViewResponse> viewPage(@PathVariable String userPk){
		return ResponseEntity.ok().body(userService.viewPage(userPk));
	}
}
