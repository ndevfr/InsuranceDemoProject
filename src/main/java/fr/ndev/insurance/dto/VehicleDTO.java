package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class VehicleDTO {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Schema(type = "String", example = "Citroen")
    private String brand;

    @NotBlank
    @Schema(type = "String", example = "Clio")
    private String model;

    @Positive
    @Schema(type = "int", example = "2022")
    private int year;

    @NotBlank
    @Schema(type = "String", example = "AA-123-AA")
    private String registrationNumber;

    @JsonIgnore
    private User user;

    public VehicleDTO() {}

    public VehicleDTO(Long id, String brand, String model, int year, String registrationNumber, User user) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.registrationNumber = registrationNumber;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle toVehicle() {
        return new Vehicle(brand, model, year, registrationNumber, user);
    }

    public static VehicleDTO of(Vehicle vehicle) {
        return new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), vehicle.getRegistrationNumber(), vehicle.getUser());
    }
}