package com.projeto.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.interactions.status.CheckStatus;

@RestController
@RequestMapping("/status")
public class StatusController {
	@Autowired
	private CheckStatus checkStatus;

	@RequestMapping(method = RequestMethod.GET)
	public String status() {
		return checkStatus.check();
	}
}
