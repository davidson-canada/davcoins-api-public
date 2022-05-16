package com.davidson.davcoinsapi.service;

import java.util.ArrayList;

import com.davidson.davcoinsapi.config.AppConfigurationProperties;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
	
    private final AppConfigurationProperties props;

	private String usernamePropsName = "username";
	private String passwordPropsName = "password";

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (props.getBo().get(this.usernamePropsName).equals(username)) {
			return new User(props.getBo().get(this.usernamePropsName), props.getBo().get(this.passwordPropsName),
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
