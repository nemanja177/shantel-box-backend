package shantel.box.services;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import shantel.box.model.ApplicationStatus;

public interface ApplicationStatusService {

	List<ApplicationStatus> findAll();
	
	ApplicationStatus findValidAppStatus();
	
	ApplicationStatus checkIsAppUnlocked(ZonedDateTime currentDate);
	
	ApplicationStatus save(ApplicationStatus status);
	
}
