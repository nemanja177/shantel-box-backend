package shantel.box.services;

import java.util.List;

import shantel.box.dto.KorisnikDTO;
import shantel.box.dto.KorisnikDTOWithoutPoints;
import shantel.box.model.Korisnik;
import shantel.box.model.UserRequest;


public interface KorisnikService {
	
	Korisnik save(UserRequest userRequest);
	
	Korisnik save(Korisnik korisnik);
	
	Korisnik findKorisnikByUsername(String username);
	
	List<Korisnik> findAll();
	
	Korisnik findKorisnikById(Integer id);
	
	void updateExpoPushToken(Integer korisnikId, String expoPushToken);
	
	String findExpoPushToken(Integer korisnikId);
	
	Korisnik findNosacPlamena();

	void assignNosacPlamena();

	List<KorisnikDTO> converToKorisnikDTO(List<Korisnik> korisnici);

	List<KorisnikDTOWithoutPoints> converToKorisnikDTOWithoutPoints(List<Korisnik> korisnici);
}
