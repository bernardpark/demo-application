package com.bpark.pasdemo.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bpark.pasdemo.mysql.entity.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}