package pasweb.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class NewClientDTO {
    @Valid
    private ClientDTO clientData;

    @NotBlank
    private String passwordRepeat;

    public NewClientDTO() {}

    public NewClientDTO(ClientDTO clientData, String passwordRepeat) {
        this.clientData = clientData;
        this.passwordRepeat = passwordRepeat;
    }

    public ClientDTO getClientData() {
        return clientData;
    }

    public void setClientData(ClientDTO clientData) {
        this.clientData = clientData;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }
}
