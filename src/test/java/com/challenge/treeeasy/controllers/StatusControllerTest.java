package com.challenge.treeeasy.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.projeto.config.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class StatusControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testStatus() {
		String body = restTemplate.getForObject( "/status", String.class );
		assertThat( body, equalTo( "OK" ) );
	}
}
