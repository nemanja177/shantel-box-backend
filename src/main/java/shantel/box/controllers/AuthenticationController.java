package shantel.box.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import shantel.box.dto.ApplicationStatusDTO;
import shantel.box.exception.ResourceConflictException;
import shantel.box.model.ApplicationStatus;
import shantel.box.model.Korisnik;
import shantel.box.model.UserRequest;
import shantel.box.model.UserTokenState;
import shantel.box.security.UserToken;
import shantel.box.security.auth.JwtAuthenticationRequest;
import shantel.box.services.ApplicationStatusService;
import shantel.box.services.KorisnikService;
import shantel.box.services.impl.KorisnikServiceImpl;


@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
public class AuthenticationController {
	
	public static final String KORISNIK_KEY = "username";
	
	@Autowired
	private ApplicationStatusService appStatusService;

	@Autowired
	private UserToken tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEndcoder;

	@Autowired
	private KorisnikServiceImpl userDetailsService;
	
	@Autowired
	private KorisnikService korisnikService;
	
//	@GetMapping
//	public void test2() {
//		System.out.println("test2");
//	}
	
	private ApplicationStatus getApplicationStatus() {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
//		System.out.println("DANASNJI DATUM: " + date);
		ApplicationStatus appStatus = appStatusService.checkIsAppUnlocked(date);
		return appStatus;
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response, HttpServletRequest request) {
		
		System.out.println("LOG: Pokusaj logina na: " + authenticationRequest.getUsername());
		ApplicationStatus appStatus = getApplicationStatus();
		if(appStatus == null || appStatus.getActive() == false || authenticationRequest.getUsername().equals("admin")) {
			Authentication authentication = null;
			try {
				authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));
			} catch (BadCredentialsException e) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			SecurityContextHolder.getContext().setAuthentication(authentication);
			Korisnik user = (Korisnik) authentication.getPrincipal();
			HttpSession session = request.getSession(true);
			session.setAttribute(AuthenticationController.KORISNIK_KEY, user.getUsername());
			String jwt = tokenUtils.generateToken(user.getUsername(), user.getAuthoritiesAsString());
			int expiresIn = tokenUtils.getExpiredIn();
//			List<String> roles = user.getAuthorities().stream()
//					.map(item -> item.getAuthority())
//					.collect(Collectors.toList());
			System.out.println("AUTORITIEZ: " + user.getAuthoritiesAsString());
			return new ResponseEntity<>(new UserTokenState(jwt, expiresIn), HttpStatus.OK);
		}
			
//			return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
		else {
			ApplicationStatusDTO appStatusDTO = new ApplicationStatusDTO(appStatus);
			return new ResponseEntity<>(appStatusDTO, HttpStatus.LOCKED);
		}
//		} else 
////			throw new ResourceConflictException(user.getId(), "User blocked!");
//		} 
	}
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping("/signup")
	public ResponseEntity<Korisnik> addUser(@RequestBody Korisnik korisnik, UriComponentsBuilder ucBuilder) {
		Korisnik existUser = this.korisnikService.findKorisnikByUsername(korisnik.getUsername());
		System.out.println(korisnik);
		if (existUser != null) {
			throw new ResourceConflictException(korisnik.getId(), "Username already exists");
		}

		Korisnik noviKorisnik = new Korisnik(korisnik.getIme(), korisnik.getPrezime(), korisnik.getUsername(), korisnik.getEmail(), passwordEndcoder.encode(korisnik.getPassword()), true, korisnik.getSlika());
		korisnikService.save(noviKorisnik);
//		HttpHeaders headers = new HttpHeaders();
//		headers.setLocation(ucBuilder.path("/dostava/korisnik/{userId}").buildAndExpand(korisnik.getId()).toUri());
		return new ResponseEntity<>(noviKorisnik, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/logout")
	public ResponseEntity<Korisnik> logout(HttpSession session) {
		session.invalidate();
		System.out.println("===================== IZLOGOVAN =================");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/validate")
	public ResponseEntity<Boolean> checkToken(HttpSession session) {
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://bigalslist.com");
		try {
			String username = (String) session.getAttribute(KORISNIK_KEY);
			Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
			korisnik.getBodovi();
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (NullPointerException e) {
			System.out.println("KORISNIK NIJE VALIDAN");
			return new ResponseEntity<>(false, HttpStatus.OK);
		}		
	}
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping(value = "/validate2")
	public ResponseEntity<?> isTokenValid(@RequestParam String token, @AuthenticationPrincipal Korisnik korisnik) {
		
//		System.out.println("LOGIN TOKEN: " + token);
//		System.out.println("KORISNIK IZ TOKENA: " + korisnik.getUsername());
		try {
			boolean isValid = tokenUtils.validateToken(token, korisnik);
			System.out.println("VALIDAN? = " + isValid);
			return ResponseEntity.ok(isValid);
		} catch (ExpiredJwtException e) {
			return ResponseEntity.ok(false);
		}
	}
	
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/lock")
	public ResponseEntity<Boolean> lockUsers(HttpSession session) {
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		try {
			for ( Korisnik korisnik: sviKorisnici) {
				korisnik.setDozvoljen(false);
				korisnikService.save(korisnik);
			}
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
		
	}
//	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/unlock")
	public ResponseEntity<Boolean> unlockUsers(HttpSession session) {
		List<Korisnik> sviKorisnici = korisnikService.findAll();
		try {
			for ( Korisnik korisnik: sviKorisnici) {
				korisnik.setDozvoljen(true);
				korisnikService.save(korisnik);
			}
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
		
	}
	@GetMapping("/password")
	public void getPassword(@RequestBody String password) {
		System.out.println(password);
		System.out.println(passwordEndcoder.encode(password));
	}
}