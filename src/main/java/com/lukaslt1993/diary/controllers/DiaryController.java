package com.lukaslt1993.diary.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lukaslt1993.diary.EndpointNames;
import com.lukaslt1993.diary.models.Record;
import com.lukaslt1993.diary.repositories.DiariesRepository;

@RestController
@RequestMapping(EndpointNames.DIARIES)
public class DiaryController {

	@Autowired
	DiariesRepository repo;

	private String getAuthor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			Object principal = auth.getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails user = (UserDetails) principal;
				return user.getUsername();
			}
		}

		return null;
	}

	@PostMapping
	public ResponseEntity<String> createRecord(@RequestBody Record rec) {
		rec.setAuthor(getAuthor());
		repo.save(rec);
		return ResponseEntity.ok("Post created");
	}

	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<String> deleteRecord(@PathVariable("id") long id) {
		Record rec = repo.findById(id).orElse(null);

		if (rec != null) {
			String author = rec.getAuthor();
			if (author.equals(getAuthor())) {
				repo.delete(rec);
				return ResponseEntity.ok("Post deleted");
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You could only delete your own posts");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
	}

	@GetMapping
	public List<Record> findAllRecords() {
		Record record = new Record();
		record.setAuthor(getAuthor());
		List<Record> result = repo.findAll(Example.of(record));
		return result;
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<String> updateRecord(@PathVariable("id") long id, @RequestBody Record record) {
		Record rec = repo.findById(id).orElse(null);

		if (rec != null) {
			String author = rec.getAuthor();
			if (author.equals(getAuthor())) {
				rec.setTitle(record.getTitle());
				rec.setText(record.getText());
				repo.save(rec);
				return ResponseEntity.ok("Post updated");
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You could only update your own posts");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
	}

}
