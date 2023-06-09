package shantel.box.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shantel.box.model.PoklonKod;

@Repository
public interface PoklonKodRepository extends JpaRepository<PoklonKod, Integer>{
	
//	PoklonKod findPoklonKodById();
	
	List<PoklonKod> findAll();
	
	List<PoklonKod> findPoklonKodBySenderId(Integer id);
	
	List<PoklonKod> findPoklonKodByReceiverIdAndActivatedDateIsNotNull(Integer id);
	
	PoklonKod findPoklonKodBybonusCode(String bonusCode);

}
