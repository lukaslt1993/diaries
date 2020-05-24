package com.lukaslt1993.diary.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lukaslt1993.diary.EndpointNames;
import com.lukaslt1993.diary.models.Author;
import com.lukaslt1993.diary.repositories.AuthorsRepository;

@RestController
public class RegisterController {

	@Autowired
	AuthorsRepository repo;

	@PostMapping(EndpointNames.REGISTER)
	public ResponseEntity<String> create(@Valid @RequestBody Author author) {
		if (!repo.existsById(author.getEmail())) {
			author.setPassword(new BCryptPasswordEncoder().encode(author.getPassword()));
			repo.save(author);
			return ResponseEntity.ok("Registration successful");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("This user already exists");
	}

}
