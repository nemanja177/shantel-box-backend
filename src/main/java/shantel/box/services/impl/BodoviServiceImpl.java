package shantel.box.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shantel.box.model.Bodovi;
import shantel.box.model.BonusNagrade;
import shantel.box.model.Korisnik;
import shantel.box.repository.BodoviRepository;
import shantel.box.services.BodoviService;
import shantel.box.services.BonusNagradeService;
import shantel.box.services.KorisnikService;

@Service
@Transactional
public class BodoviServiceImpl implements BodoviService {
	
	@Autowired
	private BodoviRepository bodoviRepository;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private BonusNagradeService bonusNagradeService;

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
//	public List<Bodovi> findWithoutSpecificSpecijalnaNagrada(String nagrade) {
//		return bodoviRepository.findBySpecijalnaNagradaIsNullOrSpecijalnaNagradaNot(nagrade); /// stari
//	}
//	@Override
//	public List<Bodovi> findLast() {
//		return bodoviRepository.mergeAll();
//	}

	@Override
	public List<Bodovi> findBySpecijalnaNagradaIsNull() {
		return bodoviRepository.findBySpecijalnaNagradaIsNull();
	}

	@Override
	public List<Bodovi> findBySpecijalnaNagrada(String nagrada) {
		return bodoviRepository.findBySpecijalnaNagrada(nagrada);
	}

	@Override
	public Bodovi findBodoviByKorisnikAndDatumDobijanja(String username, Date date) {
		return bodoviRepository.findBodoviByKorisnikAndDatumDobijanja(username, date);
	}

	@Override
	public List<Bodovi> getLastBodovi(String nagrada) {
		return bodoviRepository.findLastBodovi(nagrada);
	}
	
	@Override
	public Bodovi getLastBod(Korisnik korisnik) {
		Bodovi poslednjiBod = null;
		Set<Bodovi> korisnickiBodovi = korisnik.getBodovi();
		List<Bodovi> sortedList = new ArrayList<>(korisnickiBodovi);
		Collections.sort(sortedList);
		for( Bodovi bod : sortedList ) {
			if ( bod.getSpecijalnaNagrada() == null || bod.getSpecijalnaNagrada().equals("10 Evra")) { //  || !bod.getSpecijalnaNagrada().equalsIgnoreCase("poeni")
				poslednjiBod = bod;
			}
		}
		return poslednjiBod;
	}
	 
	@Override
	public boolean checker(Korisnik korisnik, Bodovi poslednjiBod) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date datum = new Date();
		boolean canOpen = false;
		if ( !formatter.format(datum).equals(formatter.format(poslednjiBod.getDatumDobijanja()))) {
			BonusNagrade bonusNagrada = bonusNagradeService.findEmptyReceivers(korisnik);
			try {
				if ( bonusNagrada.getReceiver() != null ) {
					canOpen = true;
				} else {
					canOpen = false;
				}
			} catch (Exception e) {
				canOpen = true;
			}
			
		} else {
			if ( poslednjiBod.getSpecijalnaNagrada() == null ) {
				canOpen = false;
			} else {
				canOpen = false;
			}
			try {
				if ( poslednjiBod.getSpecijalnaNagrada().equalsIgnoreCase("poeni") ) {
					canOpen = true;
				} else {
					canOpen = false;
				}
			} catch (Exception e) {
				canOpen = false;
			} 
		}

		return canOpen;
	}
	
	@Override
	public List<Korisnik> unoppenedBoxUsers() {
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		List<Korisnik> didntOpen = new ArrayList<>();
		
		for(Korisnik korisnik: sviKorisnici) {
			Bodovi poslednjiBod = getLastBod(korisnik);
			if ( poslednjiBod == null) {
				didntOpen.add(korisnik);
			} else if ( checker(korisnik, poslednjiBod)){	
				didntOpen.add(korisnik);
			}
		}
		
		return didntOpen;
	}
	
	@Override
	@Scheduled(cron = "0 0 0 * * ?")
	public void testSchedule() {
		
		Calendar now = Calendar.getInstance();
//		int brojDana = now.get(Calendar.DAY_OF_YEAR);
		
		now.add(Calendar.DAY_OF_MONTH, -1);
        
        // Get yesterday's date
        Date yesterdayDate = now.getTime();
		
		List<Korisnik> didntOpen = unoppenedBoxUsers();
//		Date datum = new Date();
		for (Korisnik korisnik : didntOpen) {
			if ( !korisnik.getUsername().equals("djura")) {
				Bodovi bod = new Bodovi(-200, yesterdayDate, null, korisnik);
				save(bod);
			}
			System.out.println(korisnik.getUsername());
		}
	}

}
