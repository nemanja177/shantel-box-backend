package shantel.box.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import shantel.box.model.BonusNagrade;
import shantel.box.model.Korisnik;

@Repository
public interface BonusRepository extends JpaRepository<BonusNagrade, Integer>{

	List<BonusNagrade> findAll();
	
	@Query( value = "SELECT * from bonus_nagrade ORDER BY bonus_id DESC LIMIT 7", nativeQuery = true)
	List<BonusNagrade> findLastSevenNagrade();
	
	List<BonusNagrade> findBonusNagradeByReceiver(Korisnik receiver);
	
	BonusNagrade findBonusNagradeById(Integer id);
	
	// novi nacin ( brze )
	BonusNagrade findByReceiverIsNullAndSender(Korisnik sender);
	
//	@Query(value = "SELECT * FROM bonus_nagrade bn WHERE bn.receiver IS NOT NULL AND bn.date > ?1 ")
	List<BonusNagrade> findByReceiverIsNotNull();
	
	// stari nacin 
//	@Query(value = "SELECT * FROM bonus_nagrade bn WHERE bn.receiver IS NULL", nativeQuery = true)
//	List<BonusNagrade> findEmptyReceivers();
}
