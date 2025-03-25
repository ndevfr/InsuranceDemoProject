package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.model.Address;
import fr.ndev.insurance.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class AddressDTO {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Schema(type = "string", example = "Rue Java, 42")
    private String street;

    @Schema(type = "string", example = "Apartment 404")
    private String complement;

    @NotBlank
    @Schema(type = "string", example = "72000")
    private String zipCode;

    @NotBlank
    @Schema(type = "string", example = "Le Mans")
    private String city;

    @NotBlank
    @Schema(type = "string", example = "FRANCE")
    private String country;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isMain;

    @JsonIgnore
    private User user;

    public AddressDTO() {}

    public AddressDTO(Long id, String street, String complement, String zipCode, String city, String country, boolean isMain, User user) {
        this.id = id;
        this.street = street;
        this.complement = complement;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.isMain = isMain;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address toAddress() {
        return new Address(street, complement, zipCode, city, country, isMain);
    }

    public static AddressDTO of(Address address) {
        return new AddressDTO(address.getId(), address.getStreet(), address.getComplement(), address.getZipCode(), address.getCity(), address.getCountry(), address.isMain(), address.getUser());
    }
}