package com.davidson.davcoinsapi.model;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -1L;

	private final String token;
	private final String bankUUID;

	public JwtResponse(String token, String bankUUID) {
		this.token = token;
		this.bankUUID = bankUUID;
	}
}
