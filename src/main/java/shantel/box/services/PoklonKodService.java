package shantel.box.services;

import java.util.List;

import shantel.box.model.PoklonKod;

public interface PoklonKodService {
	
	PoklonKod save(PoklonKod poklonKod);
	
	List<PoklonKod> findAllNotUsed(Integer id);
	
	List<PoklonKod> findAll();
	
	PoklonKod findKodByCode(String code);
	
	List<PoklonKod> findPoklonKodBySenderId(Integer id);
}
