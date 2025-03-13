package fr.ndev.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class ProfileRequest {

    @Schema(type = "string", example = "Marc")
    private String firstname;

    @Schema(type = "string", example = "Scout")
    private String lastname;

    @Schema(type = "string", example = "mark.scout@lumen.com")
    private String email;

    @Schema(type = "string", example = "Severance-08-927")
    private String password;

    public ProfileRequest() {}

    public ProfileRequest(String firstname, String lastname, String email, String password) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
