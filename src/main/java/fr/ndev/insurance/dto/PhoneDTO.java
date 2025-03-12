package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.model.Phone;
import fr.ndev.insurance.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class PhoneDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Schema(type = "string", example = "0123456789")
    private String phoneNumber;

    @JsonIgnore
    private boolean isMain;

    @JsonIgnore
    private User user;

    public PhoneDTO() {}

    public PhoneDTO(Long id, String phoneNumber, boolean isMain, User user) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.isMain = isMain;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonIgnore
    public boolean isMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Phone toPhone() {
        return new Phone(id, phoneNumber, isMain, user);
    }

    public static PhoneDTO of(Phone phone) {
        return new PhoneDTO(phone.getId(), phone.getPhoneNumber(), phone.isMain(), phone.getUser());
    }
}