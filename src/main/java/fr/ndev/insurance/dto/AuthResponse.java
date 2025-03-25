package fr.ndev.insurance.dto;

import fr.ndev.insurance.model.User;
import fr.ndev.insurance.enums.Role;

import java.time.Instant;
import java.util.Objects;

public class AuthResponse {
    private String email;
    private String firstname;
    private String lastname;
    private Role role;
    private boolean authenticated;
    private Instant tokenExpiresAt;

    // Default constructor for serialization
    public AuthResponse() {
    }

    // Constructor with user info
    public AuthResponse(User user, Instant tokenExpiresAt) {
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.role = user.getRole();
        this.authenticated = true;
        this.tokenExpiresAt = tokenExpiresAt;
    }

    // Constructor for failed authentications
    public AuthResponse(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthResponse that = (AuthResponse) o;
        return authenticated == that.authenticated &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, authenticated);
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role=" + role +
                ", authenticated=" + authenticated +
                ", tokenExpiresAt=" + tokenExpiresAt +
                '}';
    }
}