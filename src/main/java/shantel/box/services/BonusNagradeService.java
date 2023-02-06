package shantel.box.services;

import java.util.List;

import shantel.box.model.BonusNagrade;
import shantel.box.model.Korisnik;

public interface BonusNagradeService {

	BonusNagrade save(BonusNagrade bonusNagrade);
	
	List<BonusNagrade> findBonusNagradeByReceiver(Korisnik receiver);
	
	BonusNagrade findById(Integer id);
	
	List<BonusNagrade> findAll();
	
	BonusNagrade findEmptyReceivers(Korisnik sender);

	List<BonusNagrade> findByReceiverIsNotNull();
}
