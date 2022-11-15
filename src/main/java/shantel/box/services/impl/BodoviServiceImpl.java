package shantel.box.services.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shantel.box.model.Bodovi;
import shantel.box.repository.BodoviRepository;
import shantel.box.services.BodoviService;

@Service
public class BodoviServiceImpl implements BodoviService {
	
	@Autowired
	private BodoviRepository bodoviRepository;

	@Override
	public Bodovi save(Bodovi bod) {
		return bodoviRepository.save(bod);
	}

	@Override
	public Bodovi findBodoviByKorisnik(String username) {
		return bodoviRepository.findBodoviByKorisnik(username);
	}

	@Override
	public List<Bodovi> findAll() {
		return bodoviRepository.findAll();
	}

	@Override
	public List<Bodovi> findBodoviByDatumDobijanja(Date datum) {
		return bodoviRepository.findBodoviByDatumDobijanja(datum);
	}

//	@Override
//	public List<Bodovi> findLast() {
//		return bodoviRepository.mergeAll();
//	}

}
