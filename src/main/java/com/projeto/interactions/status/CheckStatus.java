package com.projeto.interactions.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.config.EnvironmentConfig;

@Service
public class CheckStatus {

	@Autowired
	private EnvironmentConfig environment;

	public String check() {
		return "OK";
	}
}
