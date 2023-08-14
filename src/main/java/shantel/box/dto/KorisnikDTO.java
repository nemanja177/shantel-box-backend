package shantel.box.dto;

import java.io.Serializable;

import shantel.box.model.Korisnik;


public class KorisnikDTO implements Serializable{
	private Integer id;
	private String ime;
	private String prezime;
	private String username;
	private String email;
	private String password;
	private boolean dozvoljen;
	private String slika;
	
	public KorisnikDTO(Korisnik korisnik) {
		id = korisnik.getId();
		ime = korisnik.getIme();
		prezime = korisnik.getPrezime();
		username = korisnik.getUsername();
		email = korisnik.getEmail();
		password = korisnik.getPassword();
		dozvoljen = korisnik.isDozvoljen();
		slika = korisnik.getSlika();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isDozvoljen() {
		return dozvoljen;
	}

	public void setDozvoljen(boolean dozvoljen) {
		this.dozvoljen = dozvoljen;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	@Override
	public String toString() {
		return "KorisnikDTO [id=" + id + ", ime=" + ime + ", prezime=" + prezime + ", username=" + username + ", email="
				+ email + ", password=" + password + ", dozvoljen=" + dozvoljen + ", slika=" + slika + "]";
	}
	
}
