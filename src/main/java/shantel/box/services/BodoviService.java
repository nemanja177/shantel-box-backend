package shantel.box.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import shantel.box.model.Bodovi;
import shantel.box.model.Korisnik;

public interface BodoviService {
	
//	List<Bodovi> findLast();

	Bodovi save(Bodovi bod);
	
	Bodovi findBodoviByKorisnik(String username);
	
	Bodovi findBodoviByKorisnikAndDatumDobijanja(String username, Date date);
	
	List<Bodovi> findAll();
	
	List<Bodovi> findBodoviByDatumDobijanja(Date datum);
	
	List<Bodovi> getLastBodovi(String nagrada);
	
//	List<Bodovi> findWithoutSpecificSpecijalnaNagrada(String nagrada);
	
	List<Bodovi> findBySpecijalnaNagradaIsNull();
	
	List<Bodovi> findBySpecijalnaNagrada(String nagrada);
	
	boolean checker(Korisnik korisnik, Bodovi poslednjiBod);
	
	Bodovi getLastBod(Korisnik korisnik);
	
	List<Korisnik> unoppenedBoxUsers();
	
	void testSchedule();
	
}
