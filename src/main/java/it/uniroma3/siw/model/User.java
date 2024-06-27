
package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="users")//potrebbe non funzionare a causa dell estensione!!!!!!!
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)//potrebbe non funzionare a causa dell estensione!!!!!!!
    private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	@Email
	private String email;
	@OneToOne
	private Cuoco cuoco;
	private boolean sonocuoco;
    public Long getId() {
		return id;
	}

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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public Cuoco getCuoco() {
		return cuoco;
	}

	public void setCuoco(Cuoco cuoco) {
		this.cuoco = cuoco;
	}

	public boolean isSonocuoco() {
		return sonocuoco;
	}

	public void setSonocuoco(boolean sonocuoco) {
		this.sonocuoco = sonocuoco;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
	
}

