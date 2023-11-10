package shantel.box.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import shantel.box.model.Bodovi;

@Repository
public interface BodoviRepository  extends JpaRepository<Bodovi, Integer>{
	
//	@Query(value = "SELECT bodovi.bod_id, korisnik.korisnik_id, bodovi.broj_bodova, bodovi.datum_dobijanja FROM korisnik LEFT JOIN bodovi ON korisnik.korisnik_id = bodovi.korisnik_id ORDER BY bod_id DESC LIMIT 5", nativeQuery = true)
//	List<KorisnikBodovi> mergeAll();

	Bodovi findBodoviById(Integer id);
	
	List<Bodovi> findAll();
	
	Bodovi findBodoviByKorisnik(String username);
	
	Bodovi findBodoviByKorisnikAndDatumDobijanja(String username, ZonedDateTime date);
	
	List<Bodovi> findBodoviByDatumDobijanja(Date date);
	
	@Query(value = "SELECT * from bodovi WHERE specijalna_nagrada is null OR specijalna_nagrada = ?1 ORDER BY bod_id DESC LIMIT 5", nativeQuery = true)
	List<Bodovi> findLastBodovi(String nagrada);
	
	//	List<Bodovi> findBySpecijalnaNagradaIsNullOrSpecijalnaNagradaNot(String nagrada); staro radi
	
	List<Bodovi> findBySpecijalnaNagradaIsNull();
	
	List<Bodovi> findBySpecijalnaNagrada(String nagrada);
	
}
