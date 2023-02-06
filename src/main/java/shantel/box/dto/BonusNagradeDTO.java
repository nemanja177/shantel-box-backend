package shantel.box.dto;

import shantel.box.model.BonusNagrade;
import shantel.box.model.Korisnik;

public class BonusNagradeDTO {
	private Korisnik receiver;
	private Korisnik sender;
	private Integer value;
	
	public BonusNagradeDTO(BonusNagrade bonusNagrade) {
		receiver = bonusNagrade.getReceiver();
		sender = bonusNagrade.getSender();
		value = bonusNagrade.getBonusValue();
	}

	public Korisnik getReceiver() {
		return receiver;
	}

	public void setReceiver(Korisnik receiver) {
		this.receiver = receiver;
	}

	public Korisnik getSender() {
		return sender;
	}

	public void setSender(Korisnik sender) {
		this.sender = sender;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "BonusNagradeDTO [receiver=" + receiver + ", sender=" + sender.getUsername() + ", value=" + value + "]";
	}
	
	
	
}
