package shantel.box.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "poklonKod")
public class PoklonKod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bonus_id", unique=true, nullable=false)
	private Integer id;
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "sender_id", nullable = false)
	@JsonIgnore
	private Korisnik sender;
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "receiver_id", nullable = false)
	@JsonIgnore
	private Korisnik receiver;
	
	@Column(name="bonusCode", unique = true, nullable = false)
	private String bonusCode;
	
	@Column(name = "numberOfPoints", unique = false, nullable = false)
	private int numberOfPoints;
	
	@Column(name = "generatedDate", unique = false, nullable = false)
	private Date generatedDate;
	
	@Column(name = "activatedDate", unique = false, nullable = true)
	private Date activatedDate;
	
	@Column(name = "isValid", unique = false, nullable = false)
	private Boolean isValid;
	
	public PoklonKod() {}

	public PoklonKod(Integer id, Korisnik sender, Korisnik receiver, String code, int numberOfPoints,
			Date generatedDate, Date activatedDate, Boolean isValid) {
		super();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.bonusCode = code;
		this.numberOfPoints = numberOfPoints;
		this.generatedDate = generatedDate;
		this.activatedDate = activatedDate;
		this.isValid = isValid;
	}

	public PoklonKod(Korisnik sender, Korisnik receiver, String code, int numberOfPoints, Date generatedDate,
			Boolean isValid) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.bonusCode = code;
		this.numberOfPoints = numberOfPoints;
		this.generatedDate = generatedDate;
		this.isValid = isValid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCode() {
		return bonusCode;
	}

	public void setCode(String code) {
		this.bonusCode = code;
	}

	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}

	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	public Date getActivatedDate() {
		return activatedDate;
	}

	public void setActivatedDate(Date activatedDate) {
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
		return "PoklonKod [id=" + id + ", sender=" + sender.getUsername() + ", receiver=" + receiver.getUsername() + ", code=" + bonusCode
				+ ", numberOfPoints=" + numberOfPoints + ", generatedDate=" + generatedDate + ", activatedDate="
				+ activatedDate + ", isValid=" + isValid + "]";
	}

	
	
}
