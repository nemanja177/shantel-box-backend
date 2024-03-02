package shantel.box.services.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shantel.box.dto.KorisnikDTO;
import shantel.box.dto.KorisnikDTOWithoutPoints;
import shantel.box.model.Authority;
import shantel.box.model.Bodovi;
import shantel.box.model.Korisnik;
import shantel.box.model.UserRequest;
import shantel.box.repository.BodoviRepository;
import shantel.box.repository.KorisnikRepository;
import shantel.box.services.AuthorityService;
import shantel.box.services.KorisnikService;

@Service
@Transactional
public class KorisnikServiceImpl implements KorisnikService, UserDetailsService {
	
	@Autowired
	KorisnikRepository korisnikRepository;
	
	@Autowired
	private BodoviRepository bodoviRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthorityService authService;
	
	@Override
	public Korisnik findKorisnikByUsername(String username) {
		Korisnik korisnik = korisnikRepository.findKorisnikByUsername(username);
		return korisnik;
	}
	
	@Override
	public Korisnik save(UserRequest userRequest) {
		
		Korisnik korisnik = new Korisnik();
		korisnik.setUsername(userRequest.getUsername());
		korisnik.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		korisnik.setIme(userRequest.getIme());
		korisnik.setPrezime(userRequest.getPrezime());
		korisnik.setEmail(userRequest.getEmail());
		korisnik.setDozvoljen(true);
		
		List<Authority> auth = authService.findByName("KORISNIK");
		korisnik.setAuthorities(auth);
		
		korisnik = this.korisnikRepository.save(korisnik);
		return korisnik;
	}
	@Override
	public Korisnik save(Korisnik korisnik) {
		return korisnikRepository.save(korisnik);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Korisnik korisnik = korisnikRepository.findKorisnikByUsername(username);
		if (korisnik == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return korisnik;
		}
	}
	
	@Override
	public List<Korisnik> findAll() {
		return korisnikRepository.findAll();
	}
	
	@Override
	public Korisnik findKorisnikById(Integer id) {
		Korisnik korisnik = korisnikRepository.findKorisnikById(id);
		return korisnik;
	}
	
	@Override
	public void updateExpoPushToken(Integer korisnikId, String expoPushToken) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId).orElse(null);
        if (korisnik != null) {
            korisnik.setExpoPushToken(expoPushToken);
            korisnikRepository.save(korisnik);
        }
    }

	@Override
    public String findExpoPushToken(Integer korisnikId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId).orElse(null);
        return (korisnik != null) ? korisnik.getExpoPushToken() : null;
    }

	@Override
	public Korisnik findNosacPlamena() {
		return korisnikRepository.findByNosacPlamenaTrue();
	}
	
	@Override
	public List<KorisnikDTO> converToKorisnikDTO(List<Korisnik> korisnici) {
		List<KorisnikDTO> dtoList = new ArrayList<>();
		for ( Korisnik korisnik: korisnici ) {
			dtoList.add(new KorisnikDTO(korisnik));
		}
		return dtoList;
	}
	
	@Override
	public List<KorisnikDTOWithoutPoints> converToKorisnikDTOWithoutPoints(List<Korisnik> korisnici) {
		List<KorisnikDTOWithoutPoints> dtoList = new ArrayList<>();
		for ( Korisnik korisnik: korisnici ) {
			dtoList.add(new KorisnikDTOWithoutPoints(korisnik));
		}
		return dtoList;
	}
	
	@Override
	@Scheduled(cron = "00 00 00 * * ?", zone = "Europe/Belgrade")
	public void assignNosacPlamena() {
		List<Korisnik> sviKorisnici = korisnikRepository.findAll(); 
		for ( Korisnik korisnik : sviKorisnici ) {
			if ( korisnik.isNosacPlamena() )
				korisnik.setNosacPlamena(false);
		}
		
		Random rand = new Random();
		int randomUser = rand.nextInt(sviKorisnici.size());
		Korisnik korisnik = sviKorisnici.get(randomUser);
		korisnik.setNosacPlamena(true);
		ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade");
        ZonedDateTime now = ZonedDateTime.now(desiredTimeZone);
//		Bodovi bod = new Bodovi(100, now, null, korisnik); // dodaj special tag, nemoj zaboraviti da i na frotnu mora da se filtrira ( mozda staviti Gift Sent )
//		bodoviRepository.save(bod);
        System.out.println("NOSAC PLAMENA: " + korisnik);
	}
	
}
