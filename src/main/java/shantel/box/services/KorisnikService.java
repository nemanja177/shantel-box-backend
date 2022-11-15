package shantel.box.services;

import java.util.List;

import shantel.box.model.Korisnik;
import shantel.box.model.UserRequest;


public interface KorisnikService {
	
	Korisnik save(UserRequest userRequest);
	
	Korisnik save(Korisnik korisnik);
	
	Korisnik findKorisnikByUsername(String username);
	
	List<Korisnik> findAll();
	
	Korisnik findKorisnikById(Integer id);
}
