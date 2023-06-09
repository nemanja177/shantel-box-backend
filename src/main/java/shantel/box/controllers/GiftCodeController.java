package shantel.box.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shantel.box.dto.PoklonKodDTO;
import shantel.box.model.Bodovi;
import shantel.box.model.Korisnik;
import shantel.box.model.PoklonKod;
import shantel.box.services.BodoviService;
import shantel.box.services.KorisnikService;
import shantel.box.services.PoklonKodService;

@RestController
@RequestMapping(value = "/giftCode")
public class GiftCodeController {

	@Autowired
	private PoklonKodService poklonKodService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private BodoviService bodoviService;
	
	@SuppressWarnings("null")
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping(value = "/getCode")
	public ResponseEntity<PoklonKodDTO> getKod(@AuthenticationPrincipal Korisnik sender) {
		
		List<PoklonKod> poklonKodovi = poklonKodService.findPoklonKodBySenderId(sender.getId());
		PoklonKod poklonKod = null;
		if ( poklonKodovi.size() > 0 ) {
			 poklonKod = poklonKodovi.get(poklonKodovi.size() - 1);
		} 
//		else {
//			poklonKod = poklonKodovi.get(poklonKodovi.size());
//		}
		
		// stavlja da poklon kod nije validan ukoliko je trenutan datum drugaciji do datuma generisanja
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		if ( poklonKod != null && poklonKod.getActivatedDate() == null && formatter.format(date).equals(formatter.format(poklonKod.getGeneratedDate()))) {
			poklonKod.setIsValid(false);
		}
		
		if ( poklonKod == null || poklonKod.getActivatedDate() != null && poklonKod.getIsValid() == false) {
			List<Korisnik> sviKorisnici = korisnikService.findAll(); 
			Random rand = new Random();
			int randomUser = rand.nextInt(sviKorisnici.size());
			Korisnik receiver = sviKorisnici.get(randomUser);
			
			List<String> codesList = readCode();
			String code = generateCode(codesList);
			
			Random rand2 = new Random();
			int randomPoints = rand2.nextInt(31) - 10;
			
			Date datumDobijanja = new Date();
			
			PoklonKod newPoklonKod = new PoklonKod(sender, receiver, code, randomPoints, datumDobijanja, true);
			poklonKodService.save(newPoklonKod);
			return new ResponseEntity<PoklonKodDTO>(new PoklonKodDTO(newPoklonKod), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<PoklonKodDTO>(new PoklonKodDTO(poklonKod), HttpStatus.OK);
		}
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
	
	public String generateCode(List<String> codesList) {
		String specialChars = "!@#$^*()_+=-";
		Random rand = new Random();
		int randomCodeIndex = rand.nextInt(codesList.size());
		int randomSpecChar = rand.nextInt(specialChars.length() - 1);
		int randomSpecChar2 = rand.nextInt(specialChars.length() - 1);
		int randomSpecChar3 = rand.nextInt(specialChars.length() - 1);
		int randomNumber = rand.nextInt(10000);
//		String code = codesList.get(randomCodeIndex) + specialChars.substring(randomSpecChar) + specialChars.substring(randomSpecChar2) + randomNumber + specialChars.substring(randomSpecChar3);
		String code = codesList.get(randomCodeIndex) + Character.toString(specialChars.charAt(randomSpecChar)) + randomNumber + Character.toString(specialChars.charAt(randomSpecChar2)) + Character.toString(specialChars.charAt(randomSpecChar3));
		return code;
	}
	
	
	public boolean validateKod(PoklonKod poklonKod, Korisnik receiver) {
		return false;
	}
	
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping(value = "/activateCode")
	public ResponseEntity<?> validateKod(@AuthenticationPrincipal Korisnik receiver, @RequestBody String giftCode) {
		try {	
			JSONObject jsonstring = new JSONObject(giftCode);
			String code = (String) jsonstring.get("giftcode"); // wtf
			PoklonKod poklonKod = poklonKodService.findKodByCode(code);
			System.out.println(receiver + " ==== " + jsonstring.get("giftcode") );  /// stavi da istenke ukoliko je trenutan datum veci od dana kad je kod generisan + 1 dan -- stavljeno
			if (poklonKod.getIsValid()) {
				if (poklonKod.getReceiver().getUsername().equals(receiver.getUsername())) {
					Date datumAktiviranja = new Date();
					Bodovi poklonBod = new Bodovi(poklonKod.getNumberOfPoints(), datumAktiviranja, "Gift", receiver);
					Bodovi bodZaPosiljaoca = new Bodovi(30, datumAktiviranja, "Sent Gift", poklonKod.getSender());
					poklonKod.setActivatedDate(datumAktiviranja);
					poklonKod.setIsValid(false);
					poklonKodService.save(poklonKod);
					bodoviService.save(poklonBod);
					bodoviService.save(bodZaPosiljaoca);
					return new ResponseEntity<>(true, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
				}
			} else {
				return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
	}
}