package com.bpark.pasdemo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bpark.pasdemo.form.GuestForm;
import com.bpark.pasdemo.redis.repository.GuestRepository;


@Controller
public class GuestbookController {
	
	@Autowired
	GuestRepository guestRepository;
	
	private static AtomicInteger ID_GENERATOR = new AtomicInteger( 1000 );

	@GetMapping( "/guestbook-add" )
	public String getGuestbookAdd( Model model ) {
		model.addAttribute( "guestForm", new GuestForm() );
		return "guestbook-add";
	}

	@PostMapping( "/guestbook-add" )
	public String postGuestbookAdd( @ModelAttribute( "guestForm" ) GuestForm guestForm, BindingResult bindingResult, Model model ) {
		if ( bindingResult.hasErrors() ) {
			return "guestbook-add";
		}
		
		guestForm.setId( ID_GENERATOR.getAndIncrement() + "" );
		guestForm.setDateAdded( new Date() );
		guestRepository.add( guestForm.getId(), guestForm );
		System.out.println( "Adding the following guest: " + guestForm.toString() );
		
		guestForm.clear();
		
		return "guestbook-add";
	}
	
	@GetMapping( "/guestbook-view" )
	public String getGuestbookView( Model model ) {
		Map<Object, Object> guestMap = guestRepository.findAllMessages();
		List<String> guests = new ArrayList<String>();
		
		for ( Object id : guestMap.keySet() ) {
			StringBuilder sb = new StringBuilder();
			GuestForm guestForm = ( GuestForm ) guestMap.get( id );
			sb.append( "{" );
			sb.append( "ID:" );
			sb.append( ( String ) guestForm.getId() );
			sb.append( ", First Name:" );
			sb.append( ( String ) guestForm.getFirstName() );
			sb.append( ", Last Name:" );
			sb.append( ( String ) guestForm.getLastName() );
			sb.append( ", Date Added:" );
			sb.append( ( Date ) guestForm.getDateAdded() );
			sb.append( ", Was Late?" );
			sb.append( ( boolean ) guestForm.isAreYouLate() );
			sb.append( "}" );
			guests.add( sb.toString() );
		}
		model.addAttribute( "guests", guests );
		
		return "guestbook-view";
	}
	
	@GetMapping( "/guestbook-clear" )
	public String getGuestbookClear( Model model ) {
		return "guestbook-clear";
	}	
	
	@PostMapping( "/guestbook-clear" )
	public void postGuestbookClear( Model model ) {
		guestRepository.deleteAll();
	}

}