package com.damian.repository;

import com.damian.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteDao extends JpaRepository<Note,Long> {

    public List<Note> findAll() ;
    public List<Note> findAllByAddedBy(String addedBy);


}

