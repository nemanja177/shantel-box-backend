package shantel.box.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import shantel.box.exception.ResourceConflictException;
import shantel.box.model.Korisnik;
import shantel.box.model.UserRequest;
import shantel.box.model.UserTokenState;
import shantel.box.security.UserToken;
import shantel.box.security.auth.JwtAuthenticationRequest;
import shantel.box.services.KorisnikService;
import shantel.box.services.impl.KorisnikServiceImpl;


@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
	
	public static final String KORISNIK_KEY = "username";

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
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response, HttpServletRequest request) {
		System.out.println(passwordEndcoder.encode(authenticationRequest.getPassword()));
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://bigalslist.com");
//		responseHeaders.set("Access-Control-Allow-Credentials", "true");
		
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		Korisnik user = (Korisnik) authentication.getPrincipal();
		if(user.isDozvoljen()) {
			String jwt = tokenUtils.generateToken(user.getUsername(), user.getAuthoritiesAsString());
			int expiresIn = tokenUtils.getExpiredIn();
			HttpSession session = request.getSession(true);
			session.setAttribute(AuthenticationController.KORISNIK_KEY, user.getUsername());
			System.out.println("SESSIJA LOGINA: " + session.getId());
//			try {
//				response.sendRedirect("/box/bodovi/mesecni");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			return new ResponseEntity<>(new UserTokenState(jwt, expiresIn), HttpStatus.OK);
//			return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
			
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//			throw new ResourceConflictException(user.getId(), "User blocked!");
			
		}
	}
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping("/signup")
	public ResponseEntity<Korisnik> addUser(@RequestBody Korisnik korisnik, UriComponentsBuilder ucBuilder) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		responseHeaders.set("Access-Control-Allow-Credentials", "true");
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
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		responseHeaders.set("Access-Control-Allow-Credentials", "true");
		session.invalidate();
		System.out.println("===================== IZLOGOVAN =================");
		return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
	}
	
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@GetMapping(value = "/validate")
	public ResponseEntity<Boolean> checkToken(HttpSession session) {
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "https://bigalslist.com");
		try {
			String username = (String) session.getAttribute(KORISNIK_KEY);
			Korisnik korisnik = korisnikService.findKorisnikByUsername(username);
			korisnik.getBodovi();
			System.out.println("KORISNIK: " + korisnik);
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (NullPointerException e) {
			System.out.println("KORISNIK NIJE VALIDAN");
			return new ResponseEntity<>(false, HttpStatus.OK);
		}		
	}
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
	@PostMapping(value = "/validate2")
	public ResponseEntity<?> isTokenValid(@RequestParam String token, @AuthenticationPrincipal Korisnik korisnik) {
		
		System.out.println("LOGIN TOKEN: " + token);
		System.out.println("KORISNIK IZ TOKENA: " + korisnik.getUsername());
		try {
			boolean isValid = tokenUtils.validateToken(token, korisnik);
			return ResponseEntity.ok(isValid);
		} catch (ExpiredJwtException e) {
			return ResponseEntity.ok(false);
		}
	}
	
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
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
	@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
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
	
//	@GetMapping("/test")
//	public void test() {
//		System.out.println("test");	
//	}
//	
	@GetMapping("/password")
	public void getPassword(@RequestBody String password) {
		System.out.println(password);
		System.out.println(passwordEndcoder.encode(password));
	}
}