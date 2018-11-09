package com.bpark.pasdemo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bpark.pasdemo.form.NoteForm;
import com.bpark.pasdemo.mysql.entity.Note;
import com.bpark.pasdemo.mysql.repository.NoteRepository;


@Controller
public class NotesController {
	
    @Autowired
    NoteRepository noteRepository;
    
	private static AtomicInteger ID_GENERATOR = new AtomicInteger( 1000 );

	@GetMapping( "/notes-add" )
	public String getNotesAdd( Model model ) {
		model.addAttribute( "noteForm", new NoteForm() );
		return "notes-add";
	}

	@PostMapping( "/notes-add" )
	public String postNotesAdd( @ModelAttribute( "noteForm" ) NoteForm noteForm, BindingResult bindingResult, Model model ) {
		if ( bindingResult.hasErrors() ) {
			return "notes-add";
		}
		
		Note note = new Note();
		note.setId( Long.valueOf( ID_GENERATOR.getAndIncrement() ) );
		note.setTitle( noteForm.getTitle() );
		note.setContent( noteForm.getContent() );
		note.setCreatedAt( new Date() );
		note.setUpdatedAt( new Date() );
		noteRepository.save( note );
		System.out.println( "Adding the following note: " + note.toString() );
		
		noteForm.clear();
		
		return "notes-add";
	}
	
	@GetMapping( "/notes-view" )
	public String getNotesView( Model model ) {
		List<Note> noteList = noteRepository.findAll();
		List<String> noteString = new ArrayList<String>();
		
		for ( Note note : noteList ) {
			StringBuilder sb = new StringBuilder();
			sb.append( "{" );
			sb.append( "ID:" );
			sb.append( note.getId() + "" );
			sb.append( ", Title:" );
			sb.append( ( String ) note.getTitle() );
			sb.append( ", Content:" );
			sb.append( ( String ) note.getContent() );
			sb.append( ", Date Created:" );
			sb.append( ( Date ) note.getCreatedAt() );
			sb.append( ", Date Updated:" );
			sb.append( ( Date ) note.getUpdatedAt() );
			sb.append( "}" );
			noteString.add( sb.toString() );
		}
		model.addAttribute( "notes", noteString );
		
		return "notes-view";
	}
	
	@GetMapping( "/notes-clear" )
	public String getNotesClear( Model model ) {
		return "notes-clear";
	}	
	
	@PostMapping( "/notes-clear" )
	public void Notes( Model model ) {
		noteRepository.deleteAll();
	}
	
}