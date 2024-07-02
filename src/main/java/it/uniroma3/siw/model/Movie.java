
package it.uniroma3.siw.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Movie {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
private Integer year;
private String urlImage;
private String title;
@ManyToMany(mappedBy="actorMovies")
private Set<Artist> actors;

@ManyToOne
private Artist director;
// seguono metodi setter e getter
// seguono metodi equals e hashCode:
//         due oggetti Movie sono uguali se hanno 
//         stesso titolo e stesso anno

public Set<Artist> getActor() {
	return actors;
}
public void setActor(Set<Artist> actor) {
	this.actors = actor;
}
public Artist getDirector() {
	return director;
}
public void setDirector(Artist director) {
	this.director = director;
}

public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}

public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public Integer getYear() {
	return year;
}
public void setYear(Integer year) {
	this.year = year;
}
public String getUrlImage() {
	return urlImage;
}
public void setUrlImage(String urlImage) {
	this.urlImage = urlImage;
}
@Override
public int hashCode() {
	return Objects.hash(title, year);
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Movie other = (Movie) obj;
	return Objects.equals(title, other.title) && Objects.equals(year, other.year);
}

}



