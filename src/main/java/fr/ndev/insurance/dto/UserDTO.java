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

public class UserDTO {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Schema(type = "string", example = "Mark")
    private String firstname;

    @NotBlank
    @Schema(type = "string", example = "Scout")
    private String lastname;

    @NotBlank
    @Email
    @Schema(type = "string", example = "mark.scout@lumen.com")
    private String email;

    @NotBlank
    @Schema(type = "string", example = "Severance-08-927")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;

    @NotNull
    @JsonIgnore
    private boolean locked;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<AddressDTO> addresses;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PhoneDTO> phones;

    public UserDTO() {}

    public UserDTO(long id, String firstname, String lastname, String email, String password, Role role, boolean locked, List<AddressDTO> addresses, List<PhoneDTO> phones) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
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

    public User toUser() {
        return new User(firstname, lastname, email, password, role, locked, null, null);
    }

    public static UserDTO of(User user) {
        List<AddressDTO> addressesDTO = user.getAddresses().stream().map(AddressDTO::of).collect(Collectors.toList());
        List<PhoneDTO> phonesDTO = user.getPhones().stream().map(PhoneDTO::of).collect(Collectors.toList());
        return new UserDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getRole(), user.getLocked(), addressesDTO, phonesDTO);
    }
}