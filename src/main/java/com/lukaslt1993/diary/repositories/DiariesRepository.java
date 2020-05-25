package com.lukaslt1993.diary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lukaslt1993.diary.models.Record;

@Repository
public interface DiariesRepository extends JpaRepository<Record, Long> {

}
