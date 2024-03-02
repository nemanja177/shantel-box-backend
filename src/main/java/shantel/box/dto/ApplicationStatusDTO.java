package shantel.box.dto;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

import shantel.box.model.ApplicationStatus;

public class ApplicationStatusDTO {
	
    
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
	ZoneId desiredTimeZone = ZoneId.of("Europe/Belgrade");
//	ZonedDateTime currentDateTime = ZonedDateTime.now();
	
//	DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//            .appendPattern("dd.MM.yyyy. HH:mm:ss")
//            .appendLiteral(' ')
//            .appendZoneRegionId()
//            .toFormatter();
	
	private String message;
	private ZonedDateTime dateStart;
	private ZonedDateTime dateEnd;
	
	public ApplicationStatusDTO(ApplicationStatus appStatus) {
		
		ZonedDateTime entityDateStart = appStatus.getDateStart().withZoneSameInstant(desiredTimeZone);
		ZonedDateTime entityDateEnd = appStatus.getDateEnd().withZoneSameInstant(desiredTimeZone);
		message = appStatus.getMessage();
		dateStart = entityDateStart;
		dateEnd = entityDateEnd;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ZonedDateTime getDateStart() {
		return dateStart;
	}

	public void setDateStart(ZonedDateTime dateStart) {
		this.dateStart = dateStart;
	}

	public ZonedDateTime getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(ZonedDateTime dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	
	
}
