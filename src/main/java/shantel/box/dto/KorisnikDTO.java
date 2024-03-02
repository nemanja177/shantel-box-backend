package shantel.box.dto;

import java.io.Serializable;
import java.util.Set;

import shantel.box.model.Bodovi;
import shantel.box.model.Korisnik;


public class KorisnikDTO implements Comparable<KorisnikDTO>{
	private String ime;
	private String prezime;
	private String username;
	private String email;
	private String slika;
	private Set<Bodovi> bodovi;
	
	public KorisnikDTO(Korisnik korisnik) {
		ime = korisnik.getIme();
		prezime = korisnik.getPrezime();
		username = korisnik.getUsername();
		email = korisnik.getEmail();
		slika = korisnik.getSlika();
		bodovi = korisnik.getBodovi();
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

	public Set<Bodovi> getBodovi() {
		return bodovi;
	}

	public void setBodovi(Set<Bodovi> bodovi) {
		this.bodovi = bodovi;
	}
	
	@Override
	public String toString() {
		return "KorisnikDTO [ime=" + ime + ", prezime=" + prezime + ", username=" + username + ", email=" + email
				+ ", slika=" + slika + ", bodovi=" + bodovi + "]";
	}

	@Override
	public int compareTo(KorisnikDTO o) {
		int thisBrojBodova = 0;
		int oBrojBodova = 0;
		for ( Bodovi bod: this.getBodovi()) {
			thisBrojBodova += bod.getBrojBodova();
		}
		
		for ( Bodovi bod: o.getBodovi()) {
			oBrojBodova += bod.getBrojBodova();
		}
		
		if (thisBrojBodova == oBrojBodova) {
            return 0;
        } else if (thisBrojBodova > oBrojBodova) {
            return 1;
        } else {
            return -1;
        }
	}

	
	
}
