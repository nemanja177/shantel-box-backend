package shantel.box.dto;

public class UpdateExpoPushTokenRequest {

    private Integer korisnikId;
    private String expoPushToken;
    
    public UpdateExpoPushTokenRequest() {}

	public Integer getKorisnikId() {
		return korisnikId;
	}
	
	public void setKorisnikId(Integer korisnikId) {
		this.korisnikId = korisnikId;
	}

	public String getExpoPushToken() {
		return expoPushToken;
	}
	
	public void setExpoPushToken(String expoPushToken) {
		this.expoPushToken = expoPushToken;
	}
	
	@Override
	public String toString() {
		return "UpdateExpoPushTokenRequest [poklonKodId=" + korisnikId + ", expoPushToken=" + expoPushToken + "]";
	}
	
    
}