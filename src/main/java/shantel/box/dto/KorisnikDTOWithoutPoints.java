package shantel.box.dto;

import shantel.box.model.Korisnik;

public class KorisnikDTOWithoutPoints {
	private String ime;
	private String prezime;
	private String username;
	private String email;
	private String slika;
	private Boolean nosacPlamena;
	
	public KorisnikDTOWithoutPoints(Korisnik korisnik) {
		ime = korisnik.getIme();
		prezime = korisnik.getPrezime();
		username = korisnik.getUsername();
		email = korisnik.getEmail();
		slika = korisnik.getSlika();
		nosacPlamena = korisnik.isNosacPlamena();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public Boolean getNosacPlamena() {
		return nosacPlamena;
	}

	public void setNosacPlamena(Boolean nosacPlamena) {
		this.nosacPlamena = nosacPlamena;
	}

	@Override
	public String toString() {
		return "KorisnikDTOWithoutPoints [ime=" + ime + ", prezime=" + prezime + ", username=" + username + ", email="
				+ email + ", slika=" + slika + "]";
	}
}
