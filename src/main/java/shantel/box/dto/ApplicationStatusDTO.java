package shantel.box.dto;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

import shantel.box.model.ApplicationStatus;

public class ApplicationStatusDTO {
	
    
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
//	ZonedDateTime currentDateTime = ZonedDateTime.now();
	
//	DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//            .appendPattern("dd.MM.yyyy. HH:mm:ss")
//            .appendLiteral(' ')
//            .appendZoneRegionId()
//            .toFormatter();
	
	private String message;
	private String dateStart;
	private String dateEnd;
	
	public ApplicationStatusDTO(ApplicationStatus appStatus) {
		message = appStatus.getMessage();
		dateStart = dateFormat.format(appStatus.getDateStart());
		dateEnd = dateFormat.format(appStatus.getDateEnd());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	
	
}
