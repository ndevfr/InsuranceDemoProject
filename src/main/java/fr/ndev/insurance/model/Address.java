package fr.ndev.insurance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="street")
    private String street;

    @Column(name="complement")
    private String complement;

    @Column(name="zip_code")
    private String zipCode;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="main_address")
    private boolean isMain;

    @ManyToOne @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Address() {}

    public Address(String street, String complement, String zipCode, String city, String country, boolean isMain) {
        this.street = street;
        this.complement = complement;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.isMain = isMain;
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

    public boolean isMain() { return isMain; }

    public void setIsMain(boolean isMain) { this.isMain = isMain; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}