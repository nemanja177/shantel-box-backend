package shantel.box.dto;

import shantel.box.model.Bodovi;
import shantel.box.model.Korisnik;

public class PoslednjiBodovi {

	public Korisnik korisnik;
	public Bodovi bod;
	
	public PoslednjiBodovi() {}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	public Bodovi getBod() {
		return bod;
	}

	public void setBod(Bodovi bod) {
		this.bod = bod;
	}
	
	
	
}
