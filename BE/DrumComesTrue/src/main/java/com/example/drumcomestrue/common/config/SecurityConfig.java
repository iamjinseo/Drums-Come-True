package com.example.drumcomestrue.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.example.drumcomestrue.common.OAuth.CustomOAuth2UserService;
import com.example.drumcomestrue.common.handler.LoginFailureHandler;
import com.example.drumcomestrue.common.handler.LoginSuccessHandler;
import com.example.drumcomestrue.common.handler.OAuth2LoginFailureHandler;
import com.example.drumcomestrue.common.handler.OAuth2LoginSuccessHandler;
import com.example.drumcomestrue.common.jwt.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.example.drumcomestrue.common.jwt.filter.ExceptionHandlerFilter;
import com.example.drumcomestrue.common.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.drumcomestrue.common.jwt.service.JwtService;
import com.example.drumcomestrue.common.jwt.service.LoginService;
import com.example.drumcomestrue.db.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig {

	private final JwtService jwtService;
	private final UserRepository memberRepository;
	private final ObjectMapper objectMapper;
	private final LoginService loginService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("안녕하세요???");
		http
			.formLogin().disable()
			.httpBasic().disable()
			.csrf().disable()
			.headers().frameOptions().disable()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.mvcMatchers("/").permitAll()
			.mvcMatchers(HttpMethod.POST, "/api/v1/user/signup").permitAll()
			.mvcMatchers("/api/v1/music/**").permitAll()
			.mvcMatchers(HttpMethod.POST, "/user/login").permitAll()
			.mvcMatchers(HttpMethod.GET, "/api/v1/user/login/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.oauth2Login()
			.successHandler(oAuth2LoginSuccessHandler)
			.failureHandler(oAuth2LoginFailureHandler)
			.userInfoEndpoint().userService(customOAuth2UserService);
		return http
			.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
			.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationProcessingFilter.class)
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(loginService);
		return new ProviderManager(provider);
	}

	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler(jwtService, memberRepository);
	}

	@Bean
	public LoginFailureHandler loginFailureHandler() {
		return new LoginFailureHandler();
	}

	@Bean
	public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
		CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
			= new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
		customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
		customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
		customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
		return customJsonUsernamePasswordLoginFilter;
	}

	@Bean
	public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
		return new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
	}

	@Bean
	public ExceptionHandlerFilter exceptionHandlerFilter() {
		return new ExceptionHandlerFilter(objectMapper);
	}
}
