package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.model.Phone;
import fr.ndev.insurance.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class PhoneDTO {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Schema(type = "string", example = "0123456789")
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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