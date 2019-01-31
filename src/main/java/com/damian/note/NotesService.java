package com.damian.note;

import com.damian.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private NoteDao noteDao;

    public NotesService(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public void saveNote(Note note){

        note.setAddedBy(SecurityUtils.getCurrentUserLogin());

        noteDao.save(note);
    }

    public List<Note> getNotesByUser(){
        return noteDao.findAllByAddedBy(SecurityUtils.getCurrentUserLogin());
    }

}
