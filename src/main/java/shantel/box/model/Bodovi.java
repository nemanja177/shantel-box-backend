package shantel.box.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bodovi")
public class Bodovi implements Comparable<Bodovi>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bod_id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name = "brojBodova", unique = false, nullable = false)
	private int brojBodova;
	
	@Column(name = "datumDobijanja", unique = false, nullable = false)
	private Date datumDobijanja;
	
	@Column(name = "specijalnaNagrada", unique = false, nullable = true)
	private String specijalnaNagrada;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "korisnik_id", nullable = false)
	@JsonIgnore
	private Korisnik korisnik;

	public Bodovi() {
		super();
	}

	public Bodovi(Integer id, int brojBodova, Date datumDobijanja, Korisnik korisnik) {
		super();
		this.id = id;
		this.brojBodova = brojBodova;
		this.datumDobijanja = datumDobijanja;
		this.korisnik = korisnik;
	}

	public Bodovi(int brojBodova, Date datumDobijanja, String specijalnaNagrada, Korisnik korisnik) {
		super();
		this.brojBodova = brojBodova;
		this.datumDobijanja = datumDobijanja;
		this.specijalnaNagrada = specijalnaNagrada;
		this.korisnik = korisnik;
	}

	public Bodovi(Integer id, int brojBodova, Date datumDobijanja) {
		super();
		this.id = id;
		this.brojBodova = brojBodova;
		this.datumDobijanja = datumDobijanja;
	}
	
	public Bodovi(int brojBodova, Date datumDobijanja) {
		super();
		this.brojBodova = brojBodova;
		this.datumDobijanja = datumDobijanja;
	}

	public Bodovi(Integer id, int brojBodova) {
		super();
		this.id = id;
		this.brojBodova = brojBodova;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getBrojBodova() {
		return brojBodova;
	}

	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}

	public Date getDatumDobijanja() {
		return datumDobijanja;
	}

	public void setDatumDobijanja(Date datumDobijanja) {
		this.datumDobijanja = datumDobijanja;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	public String getSpecijalnaNagrada() {
		return specijalnaNagrada;
	}

	public void setSpecijalnaNagrada(String specijalnaNagrada) {
		this.specijalnaNagrada = specijalnaNagrada;
	}

	@Override
	public String toString() {
		return "Bodovi [id=" + id + ", brojBodova=" + brojBodova + ", datumDobijanja=" + datumDobijanja + ", korisnik="
				+ korisnik + "]";
	}

	@Override
	public int compareTo(Bodovi o) {
		if (this.id == o.id) {
            return 0;
        } else if (this.id > o.id) {
            return 1;
        } else {
            return -1;
        }
	}
	
	
}
