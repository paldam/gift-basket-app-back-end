package com.damian.rest;

import com.damian.exceptions.OrderStatusException;
import com.damian.model.DbFile;
import com.damian.model.Note;
import com.damian.model.Order;
import com.damian.repository.NoteDao;
import com.damian.service.NotesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class NoteController {


    private NotesService notesService;
    private NoteDao noteDao;

    public NoteController(NotesService notesService, NoteDao noteDao) {
        this.notesService = notesService;
        this.noteDao = noteDao;
    }


    @CrossOrigin
    @GetMapping(value = "/notes")
    ResponseEntity<List<Note>> getNotes(){
        List<Note> noteList = noteDao.findAll();
        return new ResponseEntity<List<Note>>(noteList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/notes_by_current_user")
    ResponseEntity<List<Note>> getNotesbyUser(){
        List<Note> noteList = notesService.getNotesByUser();
        return new ResponseEntity<List<Note>>(noteList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/notes")
    ResponseEntity createNote(@RequestBody Note note ) {

            notesService.saveNote(note);
            return new ResponseEntity<>(note, HttpStatus.CREATED);


    }

}

