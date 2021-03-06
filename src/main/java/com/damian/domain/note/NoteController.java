package com.damian.domain.note;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

    private final NotesService notesService;
    private final NoteDao noteDao;

    public NoteController(NotesService notesService, NoteDao noteDao) {
        this.notesService = notesService;
        this.noteDao = noteDao;
    }

    @CrossOrigin
    @GetMapping(value = "/notes")
    ResponseEntity<List<Note>> getNotes() {
        List<Note> noteList = noteDao.findAllWithoutDeleted();
        return new ResponseEntity<>(noteList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/notes/{id}")
    ResponseEntity<Note> getNote(@PathVariable Long id) {
        Note note = noteDao.getOne(id);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/notes_by_current_user")
    ResponseEntity<List<Note>> getNotesByUser() {
        List<Note> noteList = notesService.getNotesByUser();
        return new ResponseEntity<>(noteList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/notes")
    ResponseEntity createNote(@RequestBody Note note) {
        notesService.saveNote(note);
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }
}

