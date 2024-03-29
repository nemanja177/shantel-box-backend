package shantel.box.controllers;

import java.io.IOException;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import shantel.box.dto.PoslednjiBodovi;
import shantel.box.model.Bodovi;
import shantel.box.model.BonusNagrade;
import shantel.box.model.Korisnik;
import shantel.box.services.BodoviService;
import shantel.box.services.BonusNagradeService;
import shantel.box.services.KorisnikService;


@RestController
@RequestMapping(value = "/bodovi")
@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
public class BodoviController {
	
	@Autowired
	private BodoviService bodoviService;
	
	@Autowired
	private BonusNagradeService bonusNagradeService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@GetMapping(value = "/jucerasnji")
	public ResponseEntity<List<Korisnik>> getJucerasnjiBodovi(HttpSession session) {
		
		
//		LocalDateTime currentDateTime = LocalDateTime.now();
//
//        LocalDate currentDate = currentDateTime.toLocalDate();
//        
//        LocalDate yesterday = currentDate.minusDays(1);
        
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); 
				
		ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
		ZonedDateTime yesterday = now.minusDays(1);
//		LocalDate todaysDate = now.toLocalDate();
		
//		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		
		List<Korisnik> korisnici = new ArrayList<>();
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
//		Calendar now = Calendar.getInstance();
//		int brojDana = now.get(Calendar.DAY_OF_YEAR);
//		int godina = now.get(Calendar.YEAR);
		
		for ( Korisnik korisnik : sviKorisnici) {
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> jucerasnjiBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
//				String yearString = yearFormat.format(bod.getDatumDobijanja());
//				LocalDate savedObjectLocalDate = bod.getDatumDobijanja().toLocalDate();
//				now.setTime(bod.getDatumDobijanja());
				if ( yesterday.getDayOfYear() == bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).getDayOfYear() && yesterday.getYear() == bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).getYear()) {
					jucerasnjiBodovi.add(bod);
				} 
			}
			korisnik.setBodovi(jucerasnjiBodovi);
			korisnici.add(korisnik);
			Collections.sort(korisnici);
			Collections.reverse(korisnici);
		}
		
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		return new ResponseEntity<>(korisnici,HttpStatus.OK);
	}
	
	@GetMapping(value = "/nedeljni")
	public ResponseEntity<List<Korisnik>> getNedeljniBodovi(HttpSession session)
        throws ServletException, IOException {
			String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
			System.out.println("=============== KORISNIK: " + username + " ===============");
//			HttpHeaders responseHeaders = new HttpHeaders();
//		    responseHeaders.set("Access-Control-Allow-Origin", "http://localhost:3000");
		
//			Calendar calendar = Calendar.getInstance();
//		    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		    String outputDate = simpleDateFormat.format(calendar.getTime());
//		    System.out.println(outputDate);
		
//			HttpSession session = request.getSession();
//			session.setAttribute(KORISNIK_KEY, "admin");
//			System.out.println((String) session.getAttribute(KORISNIK_KEY));
//			System.out.println("SESIJA: " + session.getId());
		
		    Calendar now = Calendar.getInstance();
		    int brojNedelje = now.get(Calendar.WEEK_OF_YEAR);
		    
//		    List<Bodovi> sviBodovi = bodoviService.findAll();

		    List<Korisnik> sviKorisnici = korisnikService.findAll();
		    List<Korisnik> korisnici = new ArrayList<>();
		    
//		    for (Bodovi bod: sviBodovi) {
//		    	now.setTime(bod.getDatumDobijanja());
//		    	if ( now.get(Calendar.WEEK_OF_YEAR) == brojNedelje ) {
//		    		nedeljniBodovi.add(bod);
//		    	}
//		    }
//			int month = now.get(Calendar.MONTH);
			for ( Korisnik korisnik : sviKorisnici) {
				Set<Bodovi> bodovi = korisnik.getBodovi();
			    Set<Bodovi> nedeljniBodovi = new HashSet<>();
				for ( Bodovi bod: bodovi ) {
//					now.setTime(bod.getDatumDobijanja());
					if (now.get(Calendar.WEEK_OF_YEAR) == brojNedelje) {
//						System.out.println("LAVOR " + bod);
						nedeljniBodovi.add(bod);
						
					}
				}
				korisnik.setBodovi(nedeljniBodovi);
				korisnici.add(korisnik);
//				System.out.println(korisnik);
				Collections.sort(korisnici);
				Collections.reverse(korisnici);
			}
			
			
			for ( Korisnik korisnik: korisnici) {
				System.out.println(korisnik.getIme());
			}
		    
		    
//		    System.out.println("Current week of month is : " +
//		                now.get(Calendar.WEEK_OF_YEAR));
//			return Response.status(201).entity(korisnici).header(null, korisnici)
//			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
			
		    return new ResponseEntity<>(korisnici, HttpStatus.OK);
		
        }
	
	@SuppressWarnings("deprecation")
	@GetMapping(value = "/mesecni")
	public ResponseEntity<List<Korisnik>> getMesecniBodovi(HttpSession session) {
//		System.out.println("POZVANNN");
//		HttpSession session = request.getSession();
//		session.setAttribute(KORISNIK_KEY, "admin");
//		System.out.println((String) session.getAttribute(KORISNIK_KEY));
//		System.out.println("SESIJA: " + session.getId());
//		List<Bodovi> sviBodovi = bodoviService.findAll();
		
//		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); 
		
		ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
//		ZonedDateTime yesterday = now.minusDays(1);
		
        
//		LocalDateTime currentDateTime = LocalDateTime.now();
//
//        LocalDate currentDate = currentDateTime.toLocalDate();
		
		
		List<Korisnik> korisnici = new ArrayList<>();
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
//		System.out.println("(MESEC) SESIJA: " + session.getId());
		
//		for ( Korisnik korisnik : sviKorisnici ) {
//			System.out.println(korisnik);
//		}

//		Calendar now = Calendar.getInstance();
//		int month = now.get(Calendar.MONTH);
//		int year = now.get(Calendar.YEAR);	
		
		for ( Korisnik korisnik : sviKorisnici) {
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> mesecniBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
//				System.out.println("SVI BODOVI KORISNIKA " + bod);
//				String yearString = yearFormat.format(bod.getDatumDobijanja());
//				LocalDate savedObjectLocalDate = bod.getDatumDobijanja().toLocalDate();
				if ( now.getMonth() == bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).getMonth() && now.getYear() == bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).getYear() ) {
//					System.out.println("LAVOR " + bod.getDatumDobijanja().getYear());
					mesecniBodovi.add(bod);
					
				}
			}
			korisnik.setBodovi(mesecniBodovi);
			korisnici.add(korisnik);
			Collections.sort(korisnici);
			Collections.reverse(korisnici);
//			System.out.println(korisnik);
		}
		
//		for ( Bodovi bod : sviBodovi ) {
//			if ((bod.getDatumDobijanja().getMonth()) == month) {
////				System.out.println("LAVOR " + bod);
//				mesecniBodovi.add(bod);
//			}
//		}
		
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		
		return new ResponseEntity<>(korisnici, HttpStatus.OK);
		
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping(value = "/prosliMesec")
	public ResponseEntity<List<Korisnik>> getProsliMesec(HttpSession session) {
		
//		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		
		List<Korisnik> korisnici = new ArrayList<>();
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); 
		
		ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
		
		
		ZonedDateTime oneMonthBeforeCurrentMonth = now.minusMonths(1);
		
//		Calendar now = Calendar.getInstance();
//		int month = now.get(Calendar.MONTH);
//		int year = now.get(Calendar.YEAR);
//		if ( month == 0 ) {
//			month = 12;
//		}
		
		for ( Korisnik korisnik : sviKorisnici) {
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> mesecniBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
//				String yearString = yearFormat.format(bod.getDatumDobijanja());
				if ( oneMonthBeforeCurrentMonth.getMonth() == bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).getMonth() && oneMonthBeforeCurrentMonth.getYear() == bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).getYear()  ) {
					mesecniBodovi.add(bod);
				}
			}
			korisnik.setBodovi(mesecniBodovi);
			korisnici.add(korisnik);
			Collections.sort(korisnici);
			Collections.reverse(korisnici);
		}
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		
		return new ResponseEntity<>(korisnici, HttpStatus.OK);
	}
	
	@GetMapping(value = "/lastOpen")
	public ResponseEntity<List<PoslednjiBodovi>> getLastOpened(HttpSession session) {
//		List<Bodovi> sviBodovi = bodoviService.findAll();
//		Collections.sort(sviBodovi);
//		Collections.reverse(sviBodovi);
//		List<Bodovi> bodovi = bodoviService.findLast();
//		List<KorisnikBodovi> korisnikBodovi = korisnikBodoviService.mergeAll();
//		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
	
		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();
//		System.out.println("===========BODOVI===========");
		
//		List<Bodovi> betterListBodovi = bodoviService.findWithoutSpecificSpecijalnaNagrada("Poeni"); // izmena
//		List<Bodovi> betterListBodovi = bodoviService.findBySpecijalnaNagradaIsNull();
		List<Bodovi> betterListBodovi = bodoviService.getLastBodovi("10 Evra");
//		System.out.println(betterListBodovi.size());
//		Collections.sort(betterListBodovi);
//		Collections.reverse(betterListBodovi);
//		List<Bodovi> subListBodovi = betterListBodovi.subList(0, 5);
//		for ( Bodovi bod : betterListBodovi ) {
//			System.out.println("-------------------LISTA BODOVA-------------------");
//			System.out.println(bod);
//		}
//		List<Bodovi> subListSredjeniBodovi = new ArrayList<>();
		List<PoslednjiBodovi> sredjeniBodovi = new ArrayList<>();
//		List<Bodovi> sredjeniBodovi = new ArrayList<Bodovi>();
		for ( Bodovi bod: betterListBodovi) {
//			System.out.println("------------BOD-----------");
//			System.out.println("BOD: " + bod);
			Korisnik korisnik = korisnikService.findKorisnikById(bod.getKorisnik().getId());
			PoslednjiBodovi poslednjiBod = new PoslednjiBodovi();
			poslednjiBod.setKorisnik(korisnik);
			poslednjiBod.setBod(bod);
			sredjeniBodovi.add(poslednjiBod);
//			korisnici.add(korisnik);
//			Set<Bodovi> trenutniBod = new HashSet<>();
//			trenutniBod.add(bod);
//			korisnik.setBodovi(trenutniBod);
//			korisnici.add(korisnik);
		}
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		
		
		return new ResponseEntity<>(sredjeniBodovi, HttpStatus.OK);
		
//		
//		for ( Korisnik korisnik : sviKorisnici) {
//			Set<Bodovi> bodovi = korisnik.getBodovi();
//			Set<Bodovi> dnevniBodovi = new HashSet<>();
//			for ( Bodovi bod: bodovi ) {
//				if ( formatter.format(date).equals(formatter.format(bod.getDatumDobijanja()))) {
//					dnevniBodovi.add(bod);
//				} 
//			}
//			korisnik.setBodovi(dnevniBodovi);
//			korisnici.add(korisnik);
//			Collections.sort(korisnici);
//			Collections.reverse(korisnici);
//		}
	}
	
	@GetMapping(value = "/dnevni")
	public ResponseEntity<List<Korisnik>> getDnevniBodovi(HttpSession session) {
//		HttpSession session = request.getSession();
//		session.setAttribute(KORISNIK_KEY, "admin");
//		System.out.println((String) session.getAttribute(KORISNIK_KEY));
//		System.out.println("SESIJA: " + session.getId());
//		List<Bodovi> sviBodovi = bodoviService.findAll();
	
		List<Korisnik> korisnici = new ArrayList<>();
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
//		Calendar now = Calendar.getInstance();
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); 
		
		ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
        LocalDate todaysDate = now.toLocalDate();
//		Date date = new Date();
//		System.out.println(formatter.format(date).getClass());
//		Date date2 = formatter.parse(date);
		
//		for ( Bodovi bod : sviBodovi ) {
//			System.out.println(formatter.format(bod.getDatumDobijanja()).getClass());
//			if ( formatter.format(date).equals(formatter.format(bod.getDatumDobijanja()))) {
//				System.out.println("BRAVO");
//				dnevniBodovi.add(bod);
//			}
//
//		}
		
//		LocalDateTime currentDateTime = LocalDateTime.now();
//
//        LocalDate currentDate = currentDateTime.toLocalDate();
		
		for ( Korisnik korisnik : sviKorisnici) {
//			System.out.println(korisnik.getBodovi());
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> dnevniBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
//				System.out.println("SIZE: " + korisnik.getBodovi());
//				System.out.println("bod" + bod);
				int comparisonResult = todaysDate.compareTo(bod.getDatumDobijanja().withZoneSameInstant(desiredTimeZone).toLocalDate());
				if ( comparisonResult == 0 ) {
					dnevniBodovi.add(bod);
				} 
//					else {
//					bodovi.remove(bod);
//					System.out.println("SIZE: " + korisnik.getBodovi().size());
////					if( korisnik.getBodovi().size() == 0) {
//////						System.out.println("SIZE: " + korisnik.getBodovi().size());
////						break;
////					}
//				}
			}
			korisnik.setBodovi(dnevniBodovi);
			korisnici.add(korisnik);
			Collections.sort(korisnici);
			Collections.reverse(korisnici);
//			System.out.println(korisnik);
		}
		
//		System.out.println("KORISNICI POSLATI ZA DNEVNE BODOVE " + korisnici);
		
		return new ResponseEntity<>(korisnici,HttpStatus.OK);
		
	}
	
	
	@GetMapping(value = "/check")
	public ResponseEntity<Boolean> canOpen(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
	    // Access user information
	    String username = userDetails.getUsername();
	    System.out.println("USERNAME:" + username);
//		
		Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
//		System.out.println("KORISNIK: " + korisnik);
//		System.out.println("SESIJA PROVERE: " + session.getId());
		Bodovi poslednjiBod = null;
		boolean canOpen = false;
		
		/* PREBACENO U ZASEBNU FUNCKIJU 
		 * 
		 * /- public Bodovi getLastBod -/
		 * 
		Set<Bodovi> korisnickiBodovi = korisnik.getBodovi();
		List<Bodovi> sortedList = new ArrayList<>(korisnickiBodovi);
		Collections.sort(sortedList);
		for( Bodovi bod : sortedList ) {
			if ( bod.getSpecijalnaNagrada() == null || !bod.getSpecijalnaNagrada().equalsIgnoreCase("poeni")) {
				poslednjiBod = bod;
//				System.out.println(poslednjiBod);
			}
//			System.out.println(poslednjiBod);
//			break;
		}*/
		
//		System.out.println("KORISNIK: " + formatter.format(poslednjiBod.getDatumDobijanja()));
//		System.out.println("DATUM: " + formatter.format(datum));
//		System.out.println("KORISNIK" + korisnik);
		poslednjiBod = getLastBod(korisnik);
//		poslednjiBod = bodoviService.getLastBod(korisnik);
		if ( poslednjiBod == null) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		canOpen = bodoviService.checker(korisnik, poslednjiBod);
		/* PREBACENO U ZASEBNU FUNKCIJU 
		 * /- public boolean checker -/
		 * 
		 * Date datum = new Date();
		 * 
		 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 * 
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
				canOpen = true; //verovatno false ali videcemo
			}
			if ( poslednjiBod.getSpecijalnaNagrada().equalsIgnoreCase("poeni") ) {
				canOpen = true;
			} else {
				canOpen = false;
			}
		}*/ 
		
		
		
		///stariiiiiiiii
//		if ( poslednjiBod == null ) 
//			return new ResponseEntity<>(true, HttpStatus.OK);
//		if ( !formatter.format(datum).equals(formatter.format(poslednjiBod.getDatumDobijanja()))) {
//			return new ResponseEntity<>(true, HttpStatus.OK);
//		} 
//		if ( formatter.format(datum).equals(formatter.format(poslednjiBod.getDatumDobijanja()))) {
//			if ( poslednjiBod.getSpecijalnaNagrada().equalsIgnoreCase("poeni")) {
//				System.out.println("MOZE SE OTVORITI KUTIJA, POENI DODELJENI");
//				return new ResponseEntity<>(true, HttpStatus.OK);
//			}
////			System.out.println("NE MOZE SE OTVORITI KUTIJA, POENI NISU DODELJENI");
////			return new ResponseEntity<>(false, HttpStatus.OK);	
//		}
//		BonusNagrade bonusNagrada = bonusNagradeService.findEmptyReceivers(korisnik.getId());
//		System.out.println("MRTVA BONUS NAGRADAAAAAAAAAAAAA " + bonusNagrada);
//		if (bonusNagrada.getRandomSelectedUsers() != null ) {
//			System.out.println("MOZE SE OTVORITI KUTIJA");
//			return new ResponseEntity<>(true, HttpStatus.OK);
//		}
//		else {
//			System.out.println("NEMERE");
//		}
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://bigalslist.com");
		return new ResponseEntity<>(canOpen, HttpStatus.OK);
	}
	
	public Bodovi getLastBod(Korisnik korisnik) {
		Bodovi poslednjiBod = null;
//		System.out.println("KORISNIK MRTVI: " + korisnik);
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
	
	
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping( value = "/userswhodidntnopen")
//	@PreAuthorize("hasRole('AUTHORITY_ADMIN')")
	public ResponseEntity<List<Korisnik>> getUsersWhoDidntOpen(HttpSession session) {
		
//		List<Korisnik> sviKorisnici = korisnikService.findAll();
//		List<Korisnik> didntOpen = new ArrayList<>();
//		
//		for(Korisnik korisnik: sviKorisnici) {
//			Bodovi poslednjiBod = getLastBod(korisnik);
//			if ( poslednjiBod == null) {
//				didntOpen.add(korisnik);
//			} else if ( checker(korisnik, poslednjiBod)){	
//				didntOpen.add(korisnik);
//			}
//		}
		List<Korisnik> didntOpen = bodoviService.unoppenedBoxUsers();
		
		
//		for ( Korisnik korisnik: didntOpen) {
//			System.out.println("PICKE KOJE NISU OTVORILE KUTIJU: " + korisnik);
//		}
		
		return new ResponseEntity<List<Korisnik>>(didntOpen, HttpStatus.OK);
	}
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping
	public ResponseEntity<Bodovi> setBodovi(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
		
		Bodovi bod = new Bodovi();
		Random rand = new Random();
		
		String username = userDetails.getUsername();
//		System.out.println("USERNAME:" + username);
//			
		Korisnik korisnik = korisnikService.findKorisnikByUsername(username);

		int brojBodova = rand.nextInt(300) - 100; // normal
//		int brojBodova = rand.nextInt(100) + 100; // od 100 do 200
//		int brojBodova = rand.nextInt(200); // samo do 200, bez minusa
		int specijalanBroj = rand.nextInt(300) + 1;
		
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
//		Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
		
//		if ( korisnik.getUsername().equals("")) {
//			specijalanBroj = 500;
//		}
		
//		LocalDateTime currentDateTime = LocalDateTime.now();
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade");

        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
//        LocalDate todaysDate = now.toLocalDate();
		
		bod.setDatumDobijanja(now);
		
		
		// ==== SAMO ZA JEDNU NAGRADU ==== 
		if ( specijalanBroj == 150 ) {
			bod.setBrojBodova(0);
			bod.setSpecijalnaNagrada("10 Evra");
		} else {
			bod.setBrojBodova(brojBodova);
		}
		/// ===== ZA SLUCAJ DA IMA VISE SPECIJALNIH NAGRADA =====
//		if (specijalanBroj == 500) {
//			int specBroj2 = rand.nextInt(2) + 1;
//			if ( specBroj2 == 1) {
//				bod.setBrojBodova(0);
//				bod.setSpecijalnaNagrada("Slobodan Dan");
//			} else if (specBroj2 == 2){
//				bod.setBrojBodova(0);
//				bod.setSpecijalnaNagrada("10 Evra");
//			}
//		} else {
//			bod.setBrojBodova(brojBodova);
//		}
		
//		HttpSession session = request.getSession();
//		System.out.println("SESIJA: " + session.getId());
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
////		System.out.println("SESIJA: " + session.getId());
//		Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
		if ( korisnik.getUsername().equals("Djura")) {
			bod.setBrojBodova(0);
			bod.setSpecijalnaNagrada("10 Evra");
		}
		bod.setKorisnik(korisnik);
		
		String bonusType = "Poeni";
		int bonusValue = 20;
		BonusNagrade bonusNagrada = new BonusNagrade(korisnik, bonusType, bonusValue, now);
		bodoviService.save(bod);
//		System.out.println("BONUS NAGRADA GENERACIJA: " + bonusNagrada);
		bonusNagradeService.save(bonusNagrada);
		
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		return new ResponseEntity<>(bod, HttpStatus.OK);
	}
		
//	public int getBodId(Bodovi bod) { 
//		Bodovi poslednjiBod = bodoviService.findBodoviByKorisnikAndDatumDobijanja(bod.getKorisnik().getUsername(), bod.getDatumDobijanja());
//		return poslednjiBod.getId();
//	}

	@GetMapping(value = "/random")
	public ResponseEntity<List<Integer>> random() {
		List<Integer> brojevi = new ArrayList<Integer>();
		for(int i = 0; i < 1000; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			Random rand = new Random();
			
			int brojBodova = rand.nextInt(1000);
			System.out.println(brojBodova);
			brojevi.add(brojBodova);
		}
		
		return new ResponseEntity<>(brojevi, HttpStatus.OK);
	}
	
}
