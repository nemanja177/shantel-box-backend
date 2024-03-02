package shantel.box.repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shantel.box.model.ApplicationStatus;

@Repository
public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, Integer>{

	List<ApplicationStatus> findAll();
	
	ApplicationStatus findApplicationStatusByActiveTrue();
	
	ApplicationStatus findApplicationStatusByDateEndGreaterThanEqualAndDateStartLessThanEqual(ZonedDateTime currentDate1, ZonedDateTime currentDate2);
	
//	ApplicationStatus findApplicationStatusByDateStartAfterAndDateEndBefore(Date currentDate1, Date currentDate2);
	
}
