package com.davidson.davcoinsapi.controller;

import com.davidson.davcoinsapi.config.AppConfigurationProperties;
import com.davidson.davcoinsapi.config.JwtUtility;
import com.davidson.davcoinsapi.model.JwtRequest;
import com.davidson.davcoinsapi.model.JwtResponse;
import com.davidson.davcoinsapi.service.JwtUserDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bo-login")
@RequiredArgsConstructor
public class BOLoginController {
    
	private final AuthenticationManager authenticationManager;

	private final JwtUtility jwtTokenUtil;

	private final AppConfigurationProperties props;

	private final JwtUserDetailsService userDetailsService;

	@PostMapping
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		String bankUserUUID = props.getBo().get("bank_user_uuid");

		return ResponseEntity.ok(new JwtResponse(token, bankUserUUID));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
    
}
