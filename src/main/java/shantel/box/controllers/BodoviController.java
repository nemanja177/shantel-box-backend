package shantel.box.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shantel.box.dto.PoslednjiBodovi;
import shantel.box.model.Bodovi;
import shantel.box.model.Korisnik;
import shantel.box.services.BodoviService;
import shantel.box.services.KorisnikService;


@RestController
@RequestMapping(value = "/bodovi")
public class BodoviController {
	
	@Autowired
	private BodoviService bodoviService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@GetMapping(value = "/jucerasnji")
	public ResponseEntity<List<Korisnik>> getJucerasnjiBodovi(HttpSession session) {
		
		List<Korisnik> korisnici = new ArrayList<>();
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
		Calendar now = Calendar.getInstance();
		int brojDana = now.get(Calendar.DAY_OF_YEAR);
		
		
		for ( Korisnik korisnik : sviKorisnici) {
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> jucerasnjiBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
				now.setTime(bod.getDatumDobijanja());
				if ( now.get(Calendar.DAY_OF_YEAR) == (brojDana - 1)) {
					jucerasnjiBodovi.add(bod);
				} 
			}
			korisnik.setBodovi(jucerasnjiBodovi);
			korisnici.add(korisnik);
			Collections.sort(korisnici);
			Collections.reverse(korisnici);
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		return new ResponseEntity<>(korisnici,responseHeaders,HttpStatus.OK);
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
					now.setTime(bod.getDatumDobijanja());
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
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
			
		    return new ResponseEntity<>(korisnici,responseHeaders, HttpStatus.OK);
		
        }
	
	@GetMapping(value = "/mesecni")
	public ResponseEntity<List<Korisnik>> getMesecniBodovi(HttpSession session) {
//		System.out.println("POZVANNN");
//		HttpSession session = request.getSession();
//		session.setAttribute(KORISNIK_KEY, "admin");
//		System.out.println((String) session.getAttribute(KORISNIK_KEY));
//		System.out.println("SESIJA: " + session.getId());
		List<Bodovi> sviBodovi = bodoviService.findAll();
		
		
		List<Korisnik> korisnici = new ArrayList<>();
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
//		System.out.println("(MESEC) SESIJA: " + session.getId());
		
//		for ( Korisnik korisnik : sviKorisnici ) {
//			System.out.println(korisnik);
//		}

		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH);
		for ( Korisnik korisnik : sviKorisnici) {
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> mesecniBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
//				System.out.println("SVI BODOVI KORISNIKA " + bod);
				if ((bod.getDatumDobijanja().getMonth()) == month) {
//					System.out.println("LAVOR " + bod);
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
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		
		return new ResponseEntity<>(korisnici, responseHeaders, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/prosliMesec")
	public ResponseEntity<List<Korisnik>> getProsliMesec(HttpSession session) {
		
		List<Korisnik> korisnici = new ArrayList<>();
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH);
		for ( Korisnik korisnik : sviKorisnici) {
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> mesecniBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
				if ((bod.getDatumDobijanja().getMonth()) == (month - 1)) {
//					System.out.println("LAVOR " + bod);
					mesecniBodovi.add(bod);
				}
			}
			korisnik.setBodovi(mesecniBodovi);
			korisnici.add(korisnik);
			Collections.sort(korisnici);
			Collections.reverse(korisnici);
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		
		return new ResponseEntity<>(korisnici,responseHeaders, HttpStatus.OK);
	}
	
	@GetMapping(value = "/lastOpen")
	public ResponseEntity<List<PoslednjiBodovi>> getLastOpened(HttpSession session) {
		List<Bodovi> sviBodovi = bodoviService.findAll();
		Collections.sort(sviBodovi);
		Collections.reverse(sviBodovi);
//		List<Bodovi> bodovi = bodoviService.findLast();
//		List<KorisnikBodovi> korisnikBodovi = korisnikBodoviService.mergeAll();
//		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
		List<Korisnik> korisnici = new ArrayList<>();
		
	
		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();
//		System.out.println("===========BODOVI===========");
		List<Bodovi> subListBodovi = sviBodovi.subList(0, 5);
		List<PoslednjiBodovi> sredjeniBodovi = new ArrayList<>();
//		List<Bodovi> sredjeniBodovi = new ArrayList<Bodovi>();
		for ( Bodovi bod: subListBodovi) {
			System.out.println("------------BOD-----------");
			System.out.println("BOD: " + bod);
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
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		
		
		return new ResponseEntity<>(sredjeniBodovi,responseHeaders, HttpStatus.OK);
		
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
		List<Bodovi> sviBodovi = bodoviService.findAll();
	
		List<Korisnik> korisnici = new ArrayList<>();
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar now = Calendar.getInstance();
		
		
		Date date = new Date();
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
		

		
		for ( Korisnik korisnik : sviKorisnici) {
//			System.out.println(korisnik.getBodovi());
			Set<Bodovi> bodovi = korisnik.getBodovi();
			Set<Bodovi> dnevniBodovi = new HashSet<>();
			for ( Bodovi bod: bodovi ) {
//				System.out.println("SIZE: " + korisnik.getBodovi());
//				System.out.println("bod" + bod);
				if ( formatter.format(date).equals(formatter.format(bod.getDatumDobijanja()))) {
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
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		return new ResponseEntity<>(korisnici,responseHeaders,HttpStatus.OK);
		
	}
	
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/check")
	public ResponseEntity<Boolean> canOpen(HttpSession session) {
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
		System.out.println("KORISNIK: " + korisnik.getUsername());
//		System.out.println("SESIJA PROVERE: " + session.getId());
		Bodovi poslednjiBod = null;
		Set<Bodovi> korisnickiBodovi = korisnik.getBodovi();
		List<Bodovi> sortedList = new ArrayList<>(korisnickiBodovi);
		Collections.sort(sortedList);
		for( Bodovi bod : sortedList ) {
			poslednjiBod = bod;
//			System.out.println(poslednjiBod);
//			break;
		}
		Date datum = new Date();
//		System.out.println("KORISNIK: " + formatter.format(poslednjiBod.getDatumDobijanja()));
//		System.out.println("DATUM: " + formatter.format(datum));
		if ( poslednjiBod == null ) 
			return new ResponseEntity<>(true, HttpStatus.OK);
		if ( !formatter.format(datum).equals(formatter.format(poslednjiBod.getDatumDobijanja()))) {
//			System.out.println("MORE");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
//		else {
//			System.out.println("NEMERE");
//		}
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://bigalslist.com");
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping
	public ResponseEntity<Bodovi> setBodovi(HttpSession session) {
		
		Bodovi bod = new Bodovi();
		Random rand = new Random();

		int brojBodova = rand.nextInt(100) + 1;
		int specijalanBroj = rand.nextInt(1000) + 1;
		
		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
		Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
		if ( korisnik.getUsername().equals("djura")) {
			specijalanBroj = 500;
		}
		
		Date datum = new Date();
		bod.setDatumDobijanja(datum);
		
		if (specijalanBroj == 500) {
			int specBroj2 = rand.nextInt(2) + 1;
			if ( specBroj2 == 1) {
				bod.setBrojBodova(0);
				bod.setSpecijalnaNagrada("Slobodan Dan");
			} else if (specBroj2 == 2){
				bod.setBrojBodova(0);
				bod.setSpecijalnaNagrada("10 Evra");
			}
		} else {
			bod.setBrojBodova(brojBodova);
		}
		
//		HttpSession session = request.getSession();
//		System.out.println("SESIJA: " + session.getId());
//		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
////		System.out.println("SESIJA: " + session.getId());
//		Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
//		if ( korisnik.getUsername().equals("Djura")) {
//			bod.setBrojBodova(0);
//			bod.setSpecijalnaNagrada("Slobodan Dan");
//		}
		bod.setKorisnik(korisnik);
		bodoviService.save(bod);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		return new ResponseEntity<>(bod, HttpStatus.OK);
	}
	
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
