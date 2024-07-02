
package it.uniroma3.siw.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;

@Controller
public class MovieController {
	@Autowired 
	private MovieService movieService;
	@Autowired 
	private MovieRepository movieRepository;
	@Autowired 
	private ArtistService artistService;
	@Autowired 
	private ArtistRepository artistRepository;
	
	  @GetMapping("/formNewMovie")
	    public String formNewMovie(Model model) {
	    model.addAttribute("movie", new Movie());
	    return "formNewMovie.html";
	  }
	  @GetMapping("/formFindMovie")
	    public String formFindMovie(Model model) {
	    model.addAttribute("movie", new Movie());
	    return "formFindMovie.html";
	  }

	  @PostMapping("/movies")
	  public String newMovie(@ModelAttribute("movie") Movie movie, Model model) {
	    if (!movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())) {
	      this.movieService.save(movie);
	     // model.addAttribute("movie", movie);
	      return "redirect:movie/"+movie.getId();
	    } else {
	      model.addAttribute("messaggioErrore", "Questo film esiste già");
	      return "formNewMovie.html";
	    }
	  }
	  @GetMapping("/searchMovie")
		public String search(@RequestParam("year") Integer year, Model model) {
			model.addAttribute("movies", this.movieService.findByYear(year));
			return "movies.html";
		}
	  
	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return "movie.html";
	}
	@GetMapping("/formUpdateMovie/{id}")
	public String getFormUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return "formUpdateMovie.html";
	}
	@GetMapping("/movie")
	public String getMovies(Model model) {		
		model.addAttribute("movies", this.movieService.findAll());
		return "movies.html";
	}
	@GetMapping("/movieToArtist")
	public String getMoviesToArtist(Model model) {		
		model.addAttribute("movieToArtist", this.movieService.findAll());
		return "movieToArtist.html";
	}
	@GetMapping("/addDirector/{id}")
	public String addDrirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		model.addAttribute("artists", this.artistService.findAll());
		return "addDirector.html";
	}
	@GetMapping("/addActor/{id}")
	public String addActor(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		model.addAttribute("artists", this.artistService.findAll());
		return "addActor.html";
	}
	@GetMapping("/setActor/{artistid}/{movieid}")
	public String setActor(@PathVariable("artistid") Long artistid,@PathVariable("movieid") Long movieid, Model model) {
		Artist actor=this.artistService.findById(artistid);
		Movie movie = this.movieService.findById(movieid);
		Set<Artist> actors = movie.getActor();
		actors.add(actor);
		
		List<Movie> movies = actor.getActorMovies();
		movies.add(movie);
		this.movieRepository.save(movie);
		model.addAttribute("artist", this.artistService.findById(artistid));
		model.addAttribute("movie", this.movieService.findById(movieid));
	
		
		return "formUpdateMovie.html";
	}
	
	@GetMapping("/setDirector/{artistid}/{movieid}")
	public String setDrirector(@PathVariable("artistid") Long artistid,@PathVariable("movieid") Long movieid, Model model) {
		Artist director=this.artistService.findById(artistid);
		Movie movie = this.movieService.findById(movieid);
		movie.setDirector(director);
		this.movieRepository.save(movie);
		model.addAttribute("artist", this.artistService.findById(artistid));
		model.addAttribute("movie", this.movieService.findById(movieid));
		
		return "formUpdateMovie.html";
	}

}