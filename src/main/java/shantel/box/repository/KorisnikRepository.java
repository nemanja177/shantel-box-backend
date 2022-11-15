package shantel.box.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shantel.box.model.Korisnik;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Integer>{
	
	Korisnik findKorisnikByUsername(String username);
	
	List<Korisnik> findAll();
	
	Korisnik findKorisnikById(Integer id);
}
