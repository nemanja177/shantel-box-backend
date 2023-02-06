package shantel.box.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shantel.box.model.BonusNagrade;
import shantel.box.model.Korisnik;
import shantel.box.repository.BonusRepository;
import shantel.box.services.BonusNagradeService;

@Service
public class BonusNagradeServiceImpl implements BonusNagradeService{
	
	@Autowired
	BonusRepository bonusRepository;

	@Override
	public BonusNagrade save(BonusNagrade bonusNagrade) {
		return bonusRepository.save(bonusNagrade);
	}

	@Override
	public List<BonusNagrade> findBonusNagradeByReceiver(Korisnik receiver) {
		return bonusRepository.findBonusNagradeByReceiver(receiver);
	}
	
	@Override
	public BonusNagrade findById(Integer id) {
		return bonusRepository.findBonusNagradeById(id);
	}

	@Override
	public List<BonusNagrade> findAll() {
		return bonusRepository.findAll();
	}

	@Override
	public BonusNagrade findEmptyReceivers(Korisnik sender) {
		return bonusRepository.findByReceiverIsNullAndSender(sender);
	}

	@Override
	public List<BonusNagrade> findByReceiverIsNotNull() {
		return bonusRepository.findByReceiverIsNotNull();
	}

}
