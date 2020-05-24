package com.lukaslt1993.diary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lukaslt1993.diary.models.Author;

@Repository
public interface AuthorsRepository extends JpaRepository<Author, String> {
	
}