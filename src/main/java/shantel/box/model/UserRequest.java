package shantel.box.model;

//import java.awt.Image;

public class UserRequest {
	private Integer id;
	private String ime;
	private String prezime;
	private String username;
	private String email;
	private String password;
	private boolean dozvoljen;
	private String pathSlike;
	
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
	public String getPathSlike() {
		return pathSlike;
	}
	public void setPathSlike(String pathSlike) {
		this.pathSlike = pathSlike;
	}
	@Override
	public String toString() {
		return "UserRequest [id=" + id + ", ime=" + ime + ", prezime=" + prezime + ", username=" + username + ", email="
				+ email + ", password=" + password + ", dozvoljen=" + dozvoljen + ", pathSlike=" + pathSlike + "]";
	}
	
	
	
}
