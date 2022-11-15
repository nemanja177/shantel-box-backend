//package shantel.box.model;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "korisnikBodovi")
//public class KorisnikBodovi implements Serializable{
//
//	private static final long serialVersionUID = 1L;
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name="korisnik_bodovi_id", unique=true, nullable=false)
//	private Integer id;
//	
//	@Column(name = "korisnik", unique = false, nullable = false)
//	private Korisnik korisnik;
//	@Column(name = "brojBodova", unique = false, nullable = false)
//	private int brojBodova;
//	@Column(name = "datumDobijanja", unique = false, nullable = false)
//	private Date datumDobijanja;
//	
//	public KorisnikBodovi() {}
//
//	public KorisnikBodovi(Integer id, Korisnik korisnik, int brojBodova, Date datumDobijanja) {
//		super();
//		this.id = id;
//		this.korisnik = korisnik;
//		this.brojBodova = brojBodova;
//		this.datumDobijanja = datumDobijanja;
//	}
//
//	public Korisnik getKorisnik() {
//		return korisnik;
//	}
//
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public void setKorisnik(Korisnik korisnik) {
//		this.korisnik = korisnik;
//	}
//
//	public int getBrojBodova() {
//		return brojBodova;
//	}
//
//	public void setBrojBodova(int brojBodova) {
//		this.brojBodova = brojBodova;
//	}
//
//	public Date getDatumDobijanja() {
//		return datumDobijanja;
//	}
//
//	public void setDatumDobijanja(Date datumDobijanja) {
//		this.datumDobijanja = datumDobijanja;
//	}
//
//	@Override
//	public String toString() {
//		return "KorisnikBodovi [korisnik=" + korisnik + ", brojBodova=" + brojBodova + ", datumDobijanja="
//				+ datumDobijanja + "]";
//	}
//	
//	
//	
//}
