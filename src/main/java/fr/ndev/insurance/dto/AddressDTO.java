package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.model.Address;
import fr.ndev.insurance.model.User;
import jakarta.validation.constraints.NotBlank;

public class AddressDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String street;

    private String complement;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private boolean isMain;

    @NotBlank
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean getIsMain() {
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

    public Address toAddress() {
        return new Address(street, complement, zipCode, city, country, false);
    }

    public static AddressDTO of(Address address) {
        return new AddressDTO(address.getId(), address.getStreet(), address.getComplement(), address.getZipCode(), address.getCity(), address.getCountry(), address.getIsMain(), address.getUser());
    }
}