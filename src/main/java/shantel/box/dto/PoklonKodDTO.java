package shantel.box.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import shantel.box.model.Korisnik;
import shantel.box.model.PoklonKod;

public class PoklonKodDTO {
	private Korisnik sender;
	private Korisnik receiver;
	private String bonusCode;
	private int numberOfPoints;
	private ZonedDateTime generatedDate;
	private ZonedDateTime activatedDate;
	private Boolean isValid;
	
	public PoklonKodDTO(PoklonKod poklonKod) {
		sender = poklonKod.getSender();
		receiver = poklonKod.getReceiver();
		bonusCode = poklonKod.getCode();
		numberOfPoints = poklonKod.getNumberOfPoints();
		generatedDate = poklonKod.getGeneratedDate();
		activatedDate = poklonKod.getActivatedDate();
		isValid = poklonKod.getIsValid();
	}

	public Korisnik getSender() {
		return sender;
	}

	public void setSender(Korisnik sender) {
		this.sender = sender;
	}

	public Korisnik getReceiver() {
		return receiver;
	}

	public void setReceiver(Korisnik receiver) {
		this.receiver = receiver;
	}

	public String getBonusCode() {
		return bonusCode;
	}

	public void setBonusCode(String bonusCode) {
		this.bonusCode = bonusCode;
	}

	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}

	public ZonedDateTime getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(ZonedDateTime generatedDate) {
		this.generatedDate = generatedDate;
	}

	public ZonedDateTime getActivatedDate() {
		return activatedDate;
	}

	public void setActivatedDate(ZonedDateTime activatedDate) {
		this.activatedDate = activatedDate;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return "PoklonKodDTO [sender=" + sender.getUsername() + ", receiver=" + receiver.getUsername() + ", bonusCode=" + bonusCode
				+ ", numberOfPoints=" + numberOfPoints + ", generatedDate=" + generatedDate + ", activatedDate="
				+ activatedDate + ", isValid=" + isValid + "]";
	}
	
	
}
