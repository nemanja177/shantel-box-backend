package shantel.box.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import shantel.box.dto.BonusNagradeDTO;
import shantel.box.dto.KorisnikDTO;
import shantel.box.model.Bodovi;
import shantel.box.model.BonusNagrade;
import shantel.box.model.Korisnik;
import shantel.box.services.BodoviService;
import shantel.box.services.BonusNagradeService;
import shantel.box.services.KorisnikService;

@RestController
@RequestMapping(value = "/bonusBodovi")
@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
public class BonusBodoviController {

	@Autowired
	BonusNagradeService bonusNagradeService;
	
	@Autowired
	KorisnikService korisnikService;
	
	@Autowired
	BodoviService bodoviService;
	
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/randomUsers")
	public ResponseEntity<List<Korisnik>> getRandomUsers(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
//		Korisnik sender = korisnikService.findKorisnikByUsername(username);
		
		String username = userDetails.getUsername();

		Korisnik sender = korisnikService.findKorisnikByUsername(username);
		List<Korisnik> sviKorisnici = korisnikService.findAll(); 
		List<Korisnik> randomKorisnici = new ArrayList<>();
		Random rand = new Random();
//		Korisnik sender = findCurrentKorisnik(session);
		
		System.out.println("----------------SENDER-----------------------");
		System.out.println("SENDER: " + sender.getIme());
		
		
		// stari nacin
//		System.out.println(bonusNagradeService.findEmptyReceivers());
//		List<BonusNagrade> bonusNagrade = bonusNagradeService.findEmptyReceivers();
//		BonusNagrade bonusNagrada = new BonusNagrade();
//		for ( BonusNagrade nagrade: bonusNagrade ) {
//			if ( nagrade.getSenderId() == sender.getId() ) {
//				bonusNagrada = nagrade;
//			}
//		}
		try {
			BonusNagrade bonusNagrada = bonusNagradeService.findEmptyReceivers(sender);
			if ( bonusNagrada.getRandomSelectedUsers() == null) {
				String userIds = "";
				int i = 0;
				while ( i < 3 ) {
					int randomUser = rand.nextInt(sviKorisnici.size());
					Korisnik korisnik = sviKorisnici.get(randomUser);
					if ( !randomKorisnici.contains(korisnik) && !korisnik.getUsername().equals(sender.getUsername()) && !korisnik.getUsername().equals("djura")) {
						randomKorisnici.add(korisnik);
						i++;
						userIds += korisnik.getId() + ";";
					}
				}
				bonusNagrada.setRandomSelectedUsers(userIds);
				bonusNagradeService.save(bonusNagrada);
			} else {
				String[] userIds = bonusNagrada.getRandomSelectedUsers().split(";");
				for ( int i = 0; i < userIds.length; i++) {
					Korisnik korisnik = korisnikService.findKorisnikById(Integer.parseInt(userIds[i]));
					randomKorisnici.add(korisnik);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<List<Korisnik>>(randomKorisnici, HttpStatus.NO_CONTENT);
		}
		
		
		
		
		// TODO
		// AKO ima vec generisanih korisnika, onda vratiti to. Ukoliko nema generisati nove -- uradjeno?proveriti
		// Staviti da nije moguce samog sebe dobiti kao opciju -- uradjeno ?proveriti
		return new ResponseEntity<List<Korisnik>>(randomKorisnici, HttpStatus.OK);
		
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/hasSendGift")
	public ResponseEntity<Boolean> hasSendGift(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
		String username = userDetails.getUsername();
		Korisnik sender = korisnikService.findKorisnikByUsername(username);
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
//		Korisnik sender = korisnikService.findKorisnikByUsername(username);
		System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		
		if (bonusNagradeService.findEmptyReceivers(sender) == null ) {
			System.out.println("TRUE");
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		System.out.println("FALSE");
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
//	public Korisnik findCurrentKorisnik(HttpSession session) {
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
//		Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
//		return korisnik;
//	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping(value = "/chosen")
	public ResponseEntity<Boolean> chooseUser(@RequestParam("id") int id,@AuthenticationPrincipal UserDetails userDetails ,HttpSession session) {	
		Korisnik receiver = korisnikService.findKorisnikById(id);
		String username = userDetails.getUsername();
		Korisnik sender = korisnikService.findKorisnikByUsername(username);
		System.out.println("--------------------------------------------------------------------");
		System.out.println("RECEIVER: " + receiver.getIme());
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
//		Korisnik sender = korisnikService.findKorisnikByUsername(username);
//		Korisnik sender = findCurrentKorisnik(session);
		
		
		try {
			BonusNagrade bonusNagrada = bonusNagradeService.findEmptyReceivers(sender);
			System.out.println("--------------------------------------------------------------------");
			System.out.println("Bonus Nagrada: " + bonusNagrada);
			
//			LocalDateTime currentDateTime = LocalDateTime.now();
			
			ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); // Replace with your desired time zone

	        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
			
			
//			String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
//			Korisnik sender = korisnikService.findKorisnikByUsername(username);
			
			String bonusType = "Poeni";
			int bonusValue = 20;
			
			bonusNagrada.setReceiver(receiver);
			System.out.println(bonusNagrada);
			Bodovi bod = new Bodovi(bonusValue, now, "Poeni", receiver);
			bod.setSpecijalnaNagrada(bonusType);
			bonusNagradeService.save(bonusNagrada);
			bodoviService.save(bod);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
		
		
		// TODO
		//DODAJ Repository, Service i ServiceImpl za bonusNagrade pa posle toga kreiraj objekat ovde i posalji ga na cuvanje.
		// posle toga prepravi sistem za racunanje bodova
		// END
		
	}

	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/allDailyBonuses")
	public ResponseEntity<List<BonusNagradeDTO>> getAllDailyBonus() {
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); 
		
		ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
		LocalDate todaysDate = now.toLocalDate();
		
//		Date date = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<BonusNagrade> sveBonusNagrade = bonusNagradeService.findAll();
		Collections.sort(sveBonusNagrade);
		Collections.reverse(sveBonusNagrade);
		List<BonusNagradeDTO> dtoList = new ArrayList<>();
		for (BonusNagrade nagrade: sveBonusNagrade) {
			int comparisonResult = todaysDate.compareTo(nagrade.getDate().withZoneSameInstant(desiredTimeZone).toLocalDate());
			if ( comparisonResult == 0 ) {
				dtoList.add(new BonusNagradeDTO(nagrade));
			} 
//			if ( formatter.format(nagrade.getDate()).equals(formatter.format(date))) {
//				
//			}	
		}
//		List<BonusNagradeDTO> subListBonusBodovi = dtoList.subList(0, 7);
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
		
		
		
		
		
		
		
		
		
		
//		Date date = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		List<Korisnik> korisnici = new ArrayList<>();
//		List<Korisnik> sviKorisnici = korisnikService.findAll();
//		for ( Korisnik korisnik : sviKorisnici) {
////			System.out.println(korisnik.getBodovi());
//			Set<Bodovi> bodovi = korisnik.getBodovi();
//			Set<Bodovi> bonusBodovi = new HashSet<>();
//			for ( Bodovi bod: bodovi ) {
////				System.out.println("SIZE: " + korisnik.getBodovi());
////				System.out.println("bod" + bod);
//				if ( formatter.format(date).equals(formatter.format(bod.getDatumDobijanja()))) {
//					try {
//						if (bod.getSpecijalnaNagrada().equals("Poeni")) {
//							bonusBodovi.add(bod);
//						}
//					} catch (Exception e) {
//						continue;
//					}
//					
////					System.out.println("======== BONUS BOD ======" + bod);
//				} 
////					else {
////					bodovi.remove(bod);
////					System.out.println("SIZE: " + korisnik.getBodovi().size());
//////					if( korisnik.getBodovi().size() == 0) {
////////						System.out.println("SIZE: " + korisnik.getBodovi().size());
//////						break;
//////					}
////				}
//			}
//			
//			korisnik.setBodovi(bonusBodovi);
//			korisnici.add(korisnik);
//			Collections.sort(korisnici);
//			Collections.reverse(korisnici);
//		}
//		
//		return new ResponseEntity<>(korisnici,HttpStatus.OK);
//		List<BonusNagrade> allBonusNagrade = bonusNagradeService.findByReceiverIsNotNull();
//		List<BonusNagrade> returnList = new ArrayList<>();
//		 
//		for ( BonusNagrade bonusNagrada: allBonusNagrade) {
//			if ( formatter.format(bonusNagrada.getDate()).equals(formatter.format(date))) {
//				returnList.add(bonusNagrada);
//			}
//		}
//		
//		return new ResponseEntity<List<BonusNagrade>>(returnList, HttpStatus.OK);
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/allYesterdayBonuses")
	public ResponseEntity<List<BonusNagradeDTO>> getAllYesterdayBonuses() {
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); 
		
		ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
		ZonedDateTime yesterday = now.minusDays(1);
		
//		Calendar now = Calendar.getInstance();
//		int brojDana = now.get(Calendar.DAY_OF_YEAR);
		
		List<BonusNagrade> sveBonusNagrade = bonusNagradeService.findAll();
		Collections.sort(sveBonusNagrade);
		Collections.reverse(sveBonusNagrade);
		
		List<BonusNagradeDTO> dtoList = new ArrayList<>();
		
		for (BonusNagrade nagrade: sveBonusNagrade) {
//			now.setTime(nagrade.getDate());
			if ( yesterday.getDayOfYear() == nagrade.getDate().withZoneSameInstant(desiredTimeZone).getDayOfYear() && yesterday.getYear() == nagrade.getDate().withZoneSameInstant(desiredTimeZone).getYear()) {
				dtoList.add(new BonusNagradeDTO(nagrade));
			}	
		}
		
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/allMontlyBonuses")
	public ResponseEntity<List<KorisnikDTO>> getAllBonus() {
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); 
		
		ZonedDateTime currentDate = ZonedDateTime.now(desiredTimeZone);
//		ZonedDateTime yesterday = now.minusDays(1);
		
//		LocalDateTime currentDateTime = LocalDateTime.now();
//		LocalDate currentDate = currentDateTime.toLocalDate();
		
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		List<KorisnikDTO> dtoList = new ArrayList<>();
//		Calendar now = Calendar.getInstance();
//		int month = now.get(Calendar.MONTH);
		for ( Korisnik korisnik : sviKorisnici) {
			KorisnikDTO dtoKorisnik = new KorisnikDTO(korisnik);
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> bonusBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
//				int isSame = currentDate.compareTo(bod.getDatumDobijanja().toLocalDate());
				if ( bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).getMonth() == currentDate.getMonth() && bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).getYear() == currentDate.getYear() ) {
					try {
						if (bod.getSpecijalnaNagrada().equals("Poeni")) {
							bonusBodovi.add(bod);
						}
					} catch (Exception e) {
						continue;
					}

				} 
			}
			
			dtoKorisnik.setBodovi(bonusBodovi);
			dtoList.add(dtoKorisnik);
			Collections.sort(dtoList);
			Collections.reverse(dtoList);
		}
		
		return new ResponseEntity<>(dtoList,HttpStatus.OK);
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/dailyRecentBonus")
	public ResponseEntity<List<BonusNagradeDTO>> bonusi() {
		List<BonusNagrade> sveBonusNagrade = bonusNagradeService.findLastSevenNagrade();
		Collections.sort(sveBonusNagrade);
		Collections.reverse(sveBonusNagrade);
		List<BonusNagradeDTO> dtoList = new ArrayList<>();
		for (BonusNagrade nagrade: sveBonusNagrade) {
//			if ( formatter.format(nagrade.getDate()).equals(formatter.format(date))) {
				dtoList.add(new BonusNagradeDTO(nagrade));
//			}	
		}
//		List<BonusNagradeDTO> subListBonusBodovi = dtoList.subList(0, 7);
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}
}
