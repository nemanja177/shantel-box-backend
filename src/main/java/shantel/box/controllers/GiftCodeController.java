package shantel.box.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import shantel.box.dto.KorisnikDTO;
import shantel.box.dto.KorisnikDTOWithoutPoints;
import shantel.box.dto.PoklonKodDTO;
import shantel.box.model.Bodovi;
import shantel.box.model.Korisnik;
import shantel.box.model.PoklonKod;
import shantel.box.services.BodoviService;
import shantel.box.services.KorisnikService;
import shantel.box.services.NotificationService;
import shantel.box.services.PoklonKodService;

@RestController
@RequestMapping(value = "/giftCode")
@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
public class GiftCodeController {
	
//	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private PoklonKodService poklonKodService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private BodoviService bodoviService;
	
	@Autowired
	private NotificationService notificationService;
	
//	@SuppressWarnings("null")
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping(value = "/getCode")
	@JsonIgnore
	public ResponseEntity<PoklonKodDTO> getKod(@AuthenticationPrincipal Korisnik sender, HttpSession session) {
		
		
		
		List<PoklonKod> poklonKodovi = poklonKodService.findPoklonKodBySenderId(sender.getId());
		PoklonKod poklonKod;
		if ( poklonKodovi.size() > 0 ) {
			 poklonKod = poklonKodovi.get(poklonKodovi.size() - 1);
		} else {
			poklonKod = null;
		}
//		Hibernate.initialize(sender.getBodovi());
//		else {
//			poklonKod = poklonKodovi.get(poklonKodovi.size());
//		}
//		System.out.println("POKLON KOD ======> " + poklonKod);
		// stavlja da poklon kod nije validan ukoliko je trenutan datum drugaciji do datuma generisanja

	
		
//		if ( poklonKod != null && poklonKod.getActivatedDate() == null && !formatter.format(date).equals(formatter.format(poklonKod.getGeneratedDate()))) {
//			poklonKod.setIsValid(false);
//			poklonKodService.save(poklonKod);
//			System.out.println("INFO FIRST: " + poklonKod);
//		}
		int comparisonResult = 0;
//		LocalDateTime currentDateTime = LocalDateTime.now();
//		LocalDate currentDate = currentDateTime.toLocalDate();
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade"); // Replace with your desired time zone

        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
        LocalDate todaysDate = now.toLocalDate();
		
		if ( poklonKod != null ) {
			poklonKod = checkCode(poklonKod);
			comparisonResult = todaysDate.compareTo(poklonKod.getGeneratedDate().withZoneSameInstant(desiredTimeZone).toLocalDate());
		}
			
		
		
		if ( poklonKod == null || poklonKod.getIsValid() == false) {
//			Date date = new Date();
//			System.out.println("INFO SECOND: " + poklonKod);
			if (poklonKod == null || comparisonResult != 0) {
				List<Korisnik> sviKorisnici = korisnikService.findAll(); 
				
				// for excluding debug user //
				Korisnik djura = korisnikService.findKorisnikByUsername("djura");
				sviKorisnici.remove(djura);
				// ---------------------------
//				System.out.println("SENDER: " + sender);
				
				sviKorisnici.remove(sender);
//				System.out.println("INFO THIRD: " + poklonKod);
				Random rand = new Random();
				int randomUser = rand.nextInt(sviKorisnici.size());
				Korisnik receiver = sviKorisnici.get(randomUser);
//				System.out.println("INFO FORTH: " + receiver);
				
				List<String> codesList = readCode();
				String code = generateCode(codesList, false);
				
				Random rand2 = new Random();
				int randomPoints = rand2.nextInt(41) - 10;
				
//				Date datumDobijanja = new Date();
//				Korisnik korisnik = new Korisnik(sender);
				PoklonKod newPoklonKod = new PoklonKod(sender, receiver, code, randomPoints, now, true);
				PoklonKodDTO poklonKodDTO = new PoklonKodDTO(newPoklonKod);
				poklonKodService.save(newPoklonKod);
				return new ResponseEntity<PoklonKodDTO>(poklonKodDTO, HttpStatus.OK);
			} 
		} 
		return new ResponseEntity<PoklonKodDTO>(new PoklonKodDTO(poklonKod), HttpStatus.OK);
	}
	
	
	public PoklonKod checkCode(PoklonKod poklonKod) {
		
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade");
		ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
        LocalDate todaysDate = now.toLocalDate();
//		Date date = new Date();
		int comparisonResult = todaysDate.compareTo(poklonKod.getGeneratedDate().withZoneSameInstant(desiredTimeZone).toLocalDate());
		if ( poklonKod != null && poklonKod.getActivatedDate() == null && comparisonResult != 0) {
			poklonKod.setIsValid(false);
			poklonKodService.save(poklonKod);
			System.out.println("NIJE VALIDAN: " + poklonKod);
		}
		
		return poklonKod;
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
//	@GetMapping(value = "/testwrite")
	public void writeCode() {
		File file = new File("test.txt");
		
		try {
		      File createdFile = new File("test.txt");
		      if (createdFile.createNewFile()) {
		        System.out.println("File created: " + createdFile.getAbsolutePath());
		      } else {
		        System.out.println("File already exists. " + createdFile.getAbsolutePath());
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		try {
		      FileWriter myWriter = new FileWriter(file);
		      myWriter.write("Random tekst\nLavor");
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
//	@GetMapping(value = "/testread")
	
	
	public List<String> readCode() {
		 List<String> codesList = new ArrayList<String>();
		 try {
		      File file = new File("box-codes.txt");
		      Scanner myReader = new Scanner(file);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        codesList.add(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		 return codesList;
	}
	
	public String generateCode(List<String> codesList, boolean isPlamen) {
		String specialChars = "!@#$^*()_+=-";
		Random rand = new Random();
		int randomCodeIndex = rand.nextInt(codesList.size());
		int randomSpecChar = rand.nextInt(specialChars.length() - 1);
		int randomSpecChar2 = rand.nextInt(specialChars.length() - 1);
		int randomSpecChar3 = rand.nextInt(specialChars.length() - 1);
		int randomNumber = rand.nextInt(10000);
		String code = "";
//		String code = codesList.get(randomCodeIndex) + specialChars.substring(randomSpecChar) + specialChars.substring(randomSpecChar2) + randomNumber + specialChars.substring(randomSpecChar3);
		code = codesList.get(randomCodeIndex) + Character.toString(specialChars.charAt(randomSpecChar)) + randomNumber + Character.toString(specialChars.charAt(randomSpecChar2)) + Character.toString(specialChars.charAt(randomSpecChar3));
		if ( isPlamen ) {
			code = "PLAMEN_" + codesList.get(randomCodeIndex) + Character.toString(specialChars.charAt(randomSpecChar)) + randomNumber + Character.toString(specialChars.charAt(randomSpecChar2));
		}
		return code;
	}
	
	
	
	public boolean validateKod(Korisnik receiver, PoklonKod poklonKod) {
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade");

        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
        LocalDate todaysDate = now.toLocalDate();
		
        int comparisonResult = 0;
        
		if ( poklonKod != null ) {
			poklonKod = checkCode(poklonKod);
			comparisonResult = todaysDate.compareTo(poklonKod.getGeneratedDate().withZoneSameInstant(desiredTimeZone).toLocalDate());
		}
		
		if ( poklonKod == null || poklonKod.getIsValid() == false) {
			if (poklonKod == null || comparisonResult != 0) {
				return true;
			}
			return false;
		}
		
		return false;
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping(value = "/activateCode")
	public ResponseEntity<?> validateKod(@AuthenticationPrincipal Korisnik receiver, @RequestBody String giftCode) {
		try {	
			JSONObject jsonstring = new JSONObject(giftCode);
			String code = (String) jsonstring.get("giftcode"); // wtf
			PoklonKod poklonKod = poklonKodService.findKodByCode(code);
			poklonKod = checkCode(poklonKod);
			System.out.println(receiver.getIme() + " ==== " + jsonstring.get("giftcode") );  /// stavi da istenke ukoliko je trenutan datum veci od dana kad je kod generisan + 1 dan -- stavljeno
			if (poklonKod.getIsValid()) {
				if (poklonKod.getReceiver().getUsername().equals(receiver.getUsername())) {
					
					ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade");
			        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
			        String senderPushToken = korisnikService.findExpoPushToken(poklonKod.getSender().getId());
			        System.out.println(senderPushToken);
					Bodovi poklonBod = new Bodovi(poklonKod.getNumberOfPoints(), now, "Gift", receiver);
					int brojBodova = 0;
					if ( poklonKod.getCode().contains("PLAMEN") ) {
						brojBodova = 2;
					} else {
						brojBodova = poklonKod.getNumberOfPoints();
					}
					Bodovi bodZaPosiljaoca = new Bodovi(poklonKod.getNumberOfPoints(), now, "Sent Gift", poklonKod.getSender());
					
					poklonKod.setActivatedDate(now);
					poklonKod.setIsValid(false);
					sendNotification(senderPushToken, poklonKod.getNumberOfPoints());
					poklonKodService.save(poklonKod);
					bodoviService.save(poklonBod);
					bodoviService.save(bodZaPosiljaoca);
					System.out.println("USPESNO SVE PREDJENO I DODATI BODOVI");
					return new ResponseEntity<>(poklonKod, HttpStatus.OK);
				} else {
					System.out.println("DRUGI IF NIJE PROSAO");
					return new ResponseEntity<>(false, HttpStatus.OK);
				}
			} else {
				System.out.println("PRVI IF NIJE PROSAO");
				return new ResponseEntity<>(false, HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println("CATCH: " + e);
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/showusers")
	public ResponseEntity<List<KorisnikDTOWithoutPoints>> showUsersForNosacPlamena() {
		List<Korisnik> sviKorisnici = korisnikService.findAll(); 
		Korisnik nosacPlamena = korisnikService.findNosacPlamena();
		sviKorisnici.remove(nosacPlamena);
		List<KorisnikDTOWithoutPoints> dtoKorisnici = korisnikService.converToKorisnikDTOWithoutPoints(sviKorisnici);
		return new ResponseEntity<>(dtoKorisnici, HttpStatus.OK);
	}
	
	@PostMapping(value = "/nosacGenerateCode")
	public ResponseEntity<?> nosacGenerateCode(@AuthenticationPrincipal Korisnik nosacPlamena, @RequestBody String username) {
		if ( !nosacPlamena.isNosacPlamena() ) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		Korisnik receiver = korisnikService.findKorisnikByUsername(username);
		List<PoklonKod> poklonKodovi = poklonKodService.findPoklonKodBySenderId(receiver.getId());
		PoklonKod poklonKod;
		if ( poklonKodovi.size() > 0 ) {
			 poklonKod = poklonKodovi.get(poklonKodovi.size() - 1);
		} else {
			poklonKod = null;
		}
		
		if ( validateKod(nosacPlamena, poklonKod) ) {
			List<String> codesList = readCode();
			String code = generateCode(codesList, false);
					
			ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade");
	        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
			
	        Random rand = new Random();
			int randomPoints = rand.nextInt(21);
	        
			PoklonKod newPoklonKod = new PoklonKod(nosacPlamena, receiver, code, randomPoints, now, true);
			PoklonKodDTO poklonKodDTO = new PoklonKodDTO(newPoklonKod);
			poklonKodService.save(newPoklonKod);
			return new ResponseEntity<PoklonKodDTO>(poklonKodDTO, HttpStatus.OK);
		}
		
		return new ResponseEntity<PoklonKodDTO>(new PoklonKodDTO(poklonKod), HttpStatus.OK);
		
	}
	
	public void sendNotification(String pushToken, int numberOfPoints) {
		String message = "";
		if ( numberOfPoints >= 0 ) {
			message = "Dobili ste " + numberOfPoints + " bod!";
		} else {
			message = "Izgubili ste " + numberOfPoints + " bod!";
		}
		if (pushToken != null) {
            notificationService.sendPushNotificationToSender(pushToken, "Tvoj poklon kod je aktiviran! " + message);
        }
	}
}
