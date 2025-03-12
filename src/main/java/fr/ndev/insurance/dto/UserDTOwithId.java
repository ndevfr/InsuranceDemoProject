package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOwithId {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
    private List<AddressDTO> addresses;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PhoneDTO> phones;

    public UserDTOwithId() {}

    public UserDTOwithId(long id, String firstName, String lastName, String email, String password, Role role, boolean locked, List<AddressDTO> addresses, List<PhoneDTO> phones) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.locked = locked;
        this.addresses = addresses;
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

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public User toUser() {
        return new User(firstName, lastName, email, password, role, locked, null, null);
    }

    public static UserDTOwithId of(User user) {
        List<AddressDTO> addressesDTO = user.getAddresses().stream().map(AddressDTO::of).collect(Collectors.toList());
        List<PhoneDTO> phonesDTO = user.getPhones().stream().map(PhoneDTO::of).collect(Collectors.toList());
        return new UserDTOwithId(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getRole(), user.getLocked(), addressesDTO, phonesDTO);
    }
}