package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.Address;
import fr.ndev.insurance.model.Phone;
import fr.ndev.insurance.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UserDTO {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Schema(type = "string", example = "Mark")
    private String firstName;

    @NotBlank
    @Schema(type = "string", example = "Scout")
    private String lastName;

    @NotBlank
    @Email
    @Schema(type = "string", example = "mark.scout@lumen.com")
    private String email;

    @NotBlank
    @Schema(type = "string", example = "Severance-08-927")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;

    @NotNull
    @JsonIgnore
    private boolean locked;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Address> address;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Phone> phones;

    public UserDTO() {}

    public UserDTO(long id, String firstName, String lastName, String email, String password, Role role, boolean locked, List<Address> address, List<Phone> phones) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.locked = locked;
        this.address = address;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public User toUser() {
        return new User(firstName, lastName, email, password, role, locked, address, phones);
    }

    public static UserDTO of(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getRole(), user.getLocked(), user.getAddress(), user.getPhones());
    }
}