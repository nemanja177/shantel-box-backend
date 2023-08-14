package shantel.box.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shantel.box.model.ApplicationStatus;
import shantel.box.services.ApplicationStatusService;

@RestController
@RequestMapping( value = "/appStatus")
public class ApplicationStatusController {

	@Autowired
	private ApplicationStatusService appStatusService;
	
	@PutMapping( value = "/createStatus")
	public ResponseEntity<Boolean> createNewStatus(HttpSession session, @RequestBody ApplicationStatus appStatus) {
		String username = (String) session.getAttribute(AuthenticationController.KORISNIK_KEY);
		if ( username.equals("admin") ) { // privremeno resenje 
			ApplicationStatus newAppStatus = new ApplicationStatus(appStatus.getMessage(), appStatus.getDateStart(), appStatus.getDateEnd(), true);
			appStatusService.save(newAppStatus);
			return new ResponseEntity<>(true, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
	}
}
