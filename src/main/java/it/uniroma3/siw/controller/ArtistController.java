package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.service.ArtistService;
@Controller
public class ArtistController {
	@Autowired 
	private ArtistService artistService;
	@Autowired 
	private ArtistRepository artistRepository;
	@GetMapping("/artist")
	public String getArtists(Model model) {		
		model.addAttribute("artists", this.artistService.findAll());
		return "artists.html";
	}
	
	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.findById(id));
		return "artist.html";
	}
	
}

