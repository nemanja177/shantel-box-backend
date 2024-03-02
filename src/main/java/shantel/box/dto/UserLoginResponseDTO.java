package shantel.box.dto;

import shantel.box.model.UserTokenState;

public class UserLoginResponseDTO {
    private UserTokenState userTokenState;
    private KorisnikDTO userDTO;

    public UserLoginResponseDTO(UserTokenState userTokenState, KorisnikDTO userDTO) {
        this.userTokenState = userTokenState;
        this.userDTO = userDTO;
    }

    public UserTokenState getUserTokenState() {
        return userTokenState;
    }

    public void setUserTokenState(UserTokenState userTokenState) {
        this.userTokenState = userTokenState;
    }

    public KorisnikDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(KorisnikDTO userDTO) {
        this.userDTO = userDTO;
    }
}


