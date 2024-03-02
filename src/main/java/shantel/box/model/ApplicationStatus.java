package shantel.box.model;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "application_status")
public class ApplicationStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="status_id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="status_message", unique = false, nullable = false )
	private String message;
	
	@Column(name = "date_start", unique = false, nullable = false )
	private ZonedDateTime dateStart;
	
	@Column(name = "date_end", unique = false, nullable = false )
	private ZonedDateTime dateEnd;
	
	@Column(name = "valid", unique = false, nullable = false )
	private Boolean active;
	
	public ApplicationStatus() {
		super();
	}

	public ApplicationStatus(Integer id, String message, ZonedDateTime dateStart, ZonedDateTime dateEnd, Boolean active) {
		super();
		this.id = id;
		this.message = message;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.active = active;
	}

	public ApplicationStatus(String message, ZonedDateTime dateStart, ZonedDateTime dateEnd, Boolean active) {
		super();
		this.message = message;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.active = active;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getActive() {
		return active;
	}

	public void setValid(Boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "ApplicationStatus [id=" + id + ", message=" + message + ", dateStart=" + dateStart + ", dateEnd="
				+ dateEnd + ", active=" + active + "]";
	}
	
	
}
