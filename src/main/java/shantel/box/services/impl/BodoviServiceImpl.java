package shantel.box.services.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
	public Bodovi findBodoviByKorisnikAndDatumDobijanja(String username, ZonedDateTime date) {
		return bodoviRepository.findBodoviByKorisnikAndDatumDobijanja(username, date);
	}

	@Override
	public List<Bodovi> getLastBodovi(String nagrada) {
		return bodoviRepository.findLastBodovi(nagrada);
	}
	
	@Override
	public Bodovi getLastBod(Korisnik korisnik) {
		Bodovi poslednjiBod = null;
		System.out.println("KORISNIK MRTVI: " + korisnik);
		Set<Bodovi> korisnickiBodovi = korisnik.getBodovi();
		List<Bodovi> sortedList = new ArrayList<>(korisnickiBodovi);
//		Collections.sort(sortedList);
		Collections.sort(sortedList, Collections.reverseOrder());
		for( Bodovi bod : sortedList ) {
			if ( bod.getSpecijalnaNagrada() == null || bod.getSpecijalnaNagrada().equals("10 Evra")) { //  || !bod.getSpecijalnaNagrada().equalsIgnoreCase("poeni")
				poslednjiBod = bod;
				break;
			}
		}
		return poslednjiBod;
	}
	 
	@Override
	public boolean checker(Korisnik korisnik, Bodovi poslednjiBod) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		Date datum = new Date();
//		LocalDateTime currentDateTime = LocalDateTime.now();
//		LocalDate currentDate = currentDateTime.toLocalDate();
//		System.out.println("TRENUTNO VREME: " + currentDateTime);
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); 

        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
        LocalDate todaysDate = now.toLocalDate();
		
//        System.out.println("TRENUTNO VREME SA ZONAMA: " + zonedDateTime1);
		boolean canOpen = false;
//		int comparisonResult = currentDate.compareTo(poslednjiBod.getDatumDobijanja().toLocalDate());
		System.out.println("DATUM POSLEDNJEG BODA: " + poslednjiBod.getDatumDobijanja());
		System.out.println(poslednjiBod);
		System.out.println("DATUM DANASNJI: " + todaysDate);
		System.out.println("DATUM DANASNJI SA VREMENOM: " + now);
//		System.out.println(todaysDate.isEqual(poslednjiBod.getDatumDobijanja().toLocalDate()));
		if ( !todaysDate.isEqual(poslednjiBod.getDatumDobijanja().toLocalDate())  ) {
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
				if ( poslednjiBod.getSpecijalnaNagrada().equalsIgnoreCase("poeni") || poslednjiBod.getSpecijalnaNagrada().equalsIgnoreCase("gift") || poslednjiBod.getSpecijalnaNagrada().equalsIgnoreCase("sent gift")) {
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
	@Scheduled(cron = "55 59 23 * * ?", zone = "Europe/Belgrade")
	public void testSchedule() {
		
//		Calendar now = Calendar.getInstance();
//		int brojDana = now.get(Calendar.DAY_OF_YEAR);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
//		Calendar now = Calendar.getInstance();
		
		
//		Date date = new Date();
//		
//		
//		LocalDateTime currentDateTime = LocalDateTime.now();
		
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); // Replace with your desired time zone

        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
//		now.add(Calendar.DAY_OF_MONTH, -1);
        
        // Get yesterday's date
//        Date yesterdayDate = now.getTime();
		
		List<Korisnik> didntOpen = unoppenedBoxUsers();
//		Date datum = new Date();
		for (Korisnik korisnik : didntOpen) {
			if ( !korisnik.getUsername().equals("djura")) {
				Bodovi bod = new Bodovi(-200, now, null, korisnik);
				save(bod);
			}
			System.out.println(korisnik.getUsername());
		}
	}

}
