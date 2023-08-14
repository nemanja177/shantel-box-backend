package shantel.box.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shantel.box.model.ApplicationStatus;
import shantel.box.repository.ApplicationStatusRepository;
import shantel.box.services.ApplicationStatusService;

@Service
public class ApplicationStatusServiceImpl implements ApplicationStatusService {

	@Autowired
	public ApplicationStatusRepository appStatusRepository;
	
	@Override
	public List<ApplicationStatus> findAll() {
		return appStatusRepository.findAll();
	}

	@Override
	public ApplicationStatus findValidAppStatus() {
		return appStatusRepository.findApplicationStatusByActiveTrue();
	}

	@Override
	public ApplicationStatus checkIsAppUnlocked(Date currentDate) {
		return appStatusRepository.findApplicationStatusByDateEndGreaterThanEqualAndDateStartLessThanEqual(currentDate, currentDate);
	}

	@Override
	public ApplicationStatus save(ApplicationStatus status) {
		return appStatusRepository.save(status);
	}

}
