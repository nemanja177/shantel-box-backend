package shantel.box.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import shantel.box.model.Authority;
import shantel.box.model.Korisnik;
import shantel.box.model.UserRequest;
import shantel.box.repository.KorisnikRepository;
import shantel.box.services.AuthorityService;
import shantel.box.services.KorisnikService;

@Service
public class KorisnikServiceImpl implements KorisnikService, UserDetailsService {
	
	@Autowired
	KorisnikRepository korisnikRepository;
	
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
		
		
//		Kupac kupac = new Kupac();
//		kupac.setUsername(userRequest.getUsername());
//		// pre nego sto postavimo lozinku u atribut hesiramo je
//		kupac.setPassword(passwordEncoder.encode(userRequest.getPassword()));
//		kupac.setIme(userRequest.getIme());
//		kupac.setPrezime(userRequest.getPrezime());
//		kupac.setEnabled(false);
		
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
	
}
