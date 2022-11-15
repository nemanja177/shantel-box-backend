package shantel.box.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import shantel.box.model.Bodovi;

public interface BodoviService {
	
//	List<Bodovi> findLast();

	Bodovi save(Bodovi bod);
	
	Bodovi findBodoviByKorisnik(String username);
	
	List<Bodovi> findAll();
	
	List<Bodovi> findBodoviByDatumDobijanja(Date datum);
	
}
