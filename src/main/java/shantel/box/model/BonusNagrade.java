package shantel.box.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bonusNagrade")
public class BonusNagrade implements Comparable<BonusNagrade>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bonus_id", unique=true, nullable=false)
	private Integer id;
	
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "sender_id", nullable = false, unique = false)
	@JsonIgnore
	private Korisnik sender;
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "receiver_id", nullable = true)
	@JsonIgnore
	private Korisnik receiver;
	// prebaciti da bude objekat umesto obicnog int-a
	
//	@Column(name = "sender", unique = false, nullable = false)
//	private Integer senderId;
//	
//	@Column(name = "receiver", unique = false, nullable = true)
//	private Integer receiverId;
	
	@Column(name = "bonus_type", unique = false, nullable = false)
	private String bonusType;
	
	@Column(name = "bonus_value", unique = false, nullable = false)
	private int bonusValue;
	
	@Column(name = "date", unique = false, nullable = false)
	private Date date;
	
	@Column(name = "random_selected_users", unique = false, nullable = true)
	private String randomSelectedUsers;
	
//	@Column (name = "bod_id", unique = false, nullable = false)
//	@ColumnDefault("0")
//	private Integer bodId = 0;
	
//	@OneToMany(mappedBy="bonusNagrade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private Set<Korisnik> generisaniKorisnici;

	public BonusNagrade() {
		super();
	}


	public BonusNagrade(Korisnik sender, String bonusType, int bonusValue, Date date, String randomSelectedUsers) {
		super();
		this.sender = sender;
		this.bonusType = bonusType;
		this.bonusValue = bonusValue;
		this.date = date;
		this.randomSelectedUsers = randomSelectedUsers;
	}

	public BonusNagrade(Korisnik sender, Korisnik receiver, String bonusType, int bonusValue, Date date,
		String randomSelectedUsers) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.bonusType = bonusType;
		this.bonusValue = bonusValue;
		this.date = date;
		this.randomSelectedUsers = randomSelectedUsers;
	}

	public BonusNagrade(Korisnik sender, String bonusType, int bonusValue, Date date) {
		super();
		this.sender = sender;
		this.bonusType = bonusType;
		this.bonusValue = bonusValue;
		this.date = date;
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


	public String getBonusType() {
		return bonusType;
	}


	public void setBonusType(String bonusType) {
		this.bonusType = bonusType;
	}


	public int getBonusValue() {
		return bonusValue;
	}


	public void setBonusValue(int bonusValue) {
		this.bonusValue = bonusValue;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getRandomSelectedUsers() {
		return randomSelectedUsers;
	}


	public void setRandomSelectedUsers(String randomSelectedUsers) {
		this.randomSelectedUsers = randomSelectedUsers;
	}


	@Override
	public String toString() {
		return "BonusNagrade [id=" + id + ", sender=" + sender.getUsername() + ", receiver=" + receiver + ", bonusType=" + bonusType
				+ ", bonusValue=" + bonusValue + ", date=" + date + ", randomSelectedUsers=" + randomSelectedUsers
				+ "]";
	}


	@Override
	public int compareTo(BonusNagrade o) {
		if (this.id == o.id) {
            return 0;
        } else if (this.id > o.id) {
            return 1;
        } else {
            return -1;
        }
	}

	


//	@Override
//	public int compareTo(BonusNagrade o) {
//		int thisBrojBodova = 0;
//		int oBrojBodova = 0;
//		for ( BonusNagrade bonusNagrade: this.getReceiver().get) {
//			thisBrojBodova += bonusNagrade.getBonusValue();
//		}
//		
////		System.out.println("THIS: " + thisBrojBodova);
//		
//		for ( Bodovi bod: o.getBodovi()) {
//			oBrojBodova += bod.getBrojBodova();
//		}
//		
////		System.out.println("O: " + oBrojBodova);
//		
//		if (thisBrojBodova == oBrojBodova) {
//            return 0;
//        } else if (thisBrojBodova > oBrojBodova) {
//            return 1;
//        } else {
//            return -1;
//        }
//	} 
	
}
