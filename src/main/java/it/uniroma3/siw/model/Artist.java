
package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Artist {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String surname;
	private Integer year;
	@ManyToMany
	private List<Movie> actorMovies;
	public List<Movie> getActorMovies() {
		return actorMovies;
	}
	public void setActorMovies(List<Movie> actorMovies) {
		this.actorMovies = actorMovies;
	}
	public List<Movie> getDirectorMovie() {
		return directorMovie;
	}
	public void setDirectorMovie(List<Movie> directorMovie) {
		this.directorMovie = directorMovie;
	}
	@OneToMany(mappedBy="director")
	private List<Movie> directorMovie;
	public Long getId() {
		return id;
	}
	public Artist(){}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
	return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, surname);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artist other = (Artist) obj;
		return Objects.equals(name, other.name) && Objects.equals(surname, other.surname);
	}

}

