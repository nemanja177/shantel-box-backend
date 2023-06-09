package shantel.box.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shantel.box.model.PoklonKod;
import shantel.box.repository.PoklonKodRepository;
import shantel.box.services.PoklonKodService;

@Service
public class PoklonKodServiceImpl implements PoklonKodService {
	
	@Autowired
	private PoklonKodRepository poklonKodRepository;

	@Override
	public PoklonKod save(PoklonKod poklonKod) {
		return poklonKodRepository.save(poklonKod);
	}

	@Override
	public List<PoklonKod> findAllNotUsed(Integer id) {
		return poklonKodRepository.findPoklonKodByReceiverIdAndActivatedDateIsNotNull(id);
	}

	@Override
	public List<PoklonKod> findAll() {
		return poklonKodRepository.findAll();
	}

	@Override
	public PoklonKod findKodByCode(String code) {
		return poklonKodRepository.findPoklonKodBybonusCode(code);
	}

	@Override
	public List<PoklonKod> findPoklonKodBySenderId(Integer id) {
		return poklonKodRepository.findPoklonKodBySenderId(id);
	}

}
