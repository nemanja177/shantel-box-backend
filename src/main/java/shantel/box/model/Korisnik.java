package shantel.box.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "korisnik")
public class Korisnik implements Serializable, UserDetails, Comparable<Korisnik>{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="korisnik_id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name = "ime", unique = false, nullable = false)
	private String ime;
	
	@Column(name = "prezime", unique = false, nullable = false)
	private String prezime;
	
	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "password", unique = false, nullable = false)
	private String password;
	
	@Column(name = "dozvoljen", unique = false, nullable = false)
	private boolean dozvoljen;
	
	@Column(name = "slika", unique = false, nullable = true)
	private String slika;
	
	@OneToMany(mappedBy = "korisnik", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	private Set<Bodovi> bodovi;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "korisnik_id", referencedColumnName = "korisnik_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;
	
	public Korisnik() {}

	public Korisnik(Integer id, String ime, String prezime, String username, String email, String password,
			boolean dozvoljen) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.username = username;
		this.email = email;
		this.password = password;
		this.dozvoljen = dozvoljen;
	}

	public Korisnik(Integer id, String ime, String prezime, String username, String email, String password,
			boolean dozvoljen, String slika) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.username = username;
		this.email = email;
		this.password = password;
		this.dozvoljen = dozvoljen;
		this.slika = slika;
	}
	
	public Korisnik(String ime, String prezime, String username, String email, String password,
			boolean dozvoljen, String slika) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.username = username;
		this.email = email;
		this.password = password;
		this.dozvoljen = dozvoljen;
		this.slika = slika;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isDozvoljen() {
		return dozvoljen;
	}

	public void setDozvoljen(boolean dozvoljen) {
		this.dozvoljen = dozvoljen;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	public Set<Bodovi> getBodovi() {
		return bodovi;
	}

	public void setBodovi(Set<Bodovi> bodovi) {
		this.bodovi = bodovi;
	}

	

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	@Override
	public String toString() {
		return "Korisnik [id=" + id + ", ime=" + ime + ", prezime=" + prezime + ", username=" + username + ", email="
				+ email + ", password=" + password + ", slika=" + slika + ", dozvoljen=" + dozvoljen + "]";
	}

	@JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    
    @JsonIgnore
    public String getAuthoritiesAsString() {
    	StringBuilder sb = new StringBuilder();
    	
    	for (Authority authority : this.authorities) {
    		sb.append(authority.getName() + " ");
    	}
    	
    	return sb.toString();
    }
    
    @Override
    public boolean isEnabled() {
        return dozvoljen;
    }
    
    public void setEnabled(boolean dozvoljen) {
    	this.dozvoljen = dozvoljen;
    }

	@Override
	public int compareTo(Korisnik o) {
		int thisBrojBodova = 0;
		int oBrojBodova = 0;
		for ( Bodovi bod: this.getBodovi()) {
			thisBrojBodova += bod.getBrojBodova();
		}
		
//		System.out.println("THIS: " + thisBrojBodova);
		
		for ( Bodovi bod: o.getBodovi()) {
			oBrojBodova += bod.getBrojBodova();
		}
		
//		System.out.println("O: " + oBrojBodova);
		
		if (thisBrojBodova == oBrojBodova) {
            return 0;
        } else if (thisBrojBodova > oBrojBodova) {
            return 1;
        } else {
            return -1;
        }
	}
    
	
}
