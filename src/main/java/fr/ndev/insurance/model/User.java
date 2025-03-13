package fr.ndev.insurance.model;

import fr.ndev.insurance.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstname;

    @Column(name = "last_name", nullable = false)
    private String lastname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval = true)
    @OrderBy("isMain DESC")
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval = true)
    @OrderBy("isMain DESC")
    private List<Phone> phones = new ArrayList<>();

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public User() {}

    public User(String firstname, String lastname, String email, String password, Role role, boolean locked, List<Address> addresses, List<Phone> phones) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role != null ? role : Role.CLIENT;
        this.locked = false;
        this.addresses = addresses;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public void addAddress(Address address) {
        if (this.addresses.isEmpty()) {
            address.setIsMain(true);
        }
        address.setUser(this);
        this.addresses.add(address);
    }

    public void deleteAddress(int index) {
        this.addresses.remove(index);
    }

    public void updateAddress(int index, Address address) {
        address.setUser(this);
        boolean isMain = this.addresses.get(index).isMain();
        address.setIsMain(isMain);
        this.addresses.set(index, address);
    }

    public void chooseAddressAsMain(int index) {
        this.addresses.forEach(a -> a.setIsMain(false));
        this.addresses.get(index).setIsMain(true);
    }

    public void clearAddresses() {
        this.addresses.clear();
    }

    public void addPhone(Phone phone) {
        if (this.phones.isEmpty()) {
            phone.setIsMain(true);
        }
        phone.setUser(this);
        this.phones.add(phone);
    }

    public void deletePhone(int index) {
        this.phones.remove(index);
    }

    public void updatePhone(int index, Phone phone) {
        phone.setUser(this);
        boolean isMain = this.phones.get(index).isMain();
        phone.setIsMain(isMain);
        this.phones.set(index, phone);
    }

    public void choosePhoneAsMain(int index) {
        this.phones.forEach(a -> a.setIsMain(false));
        this.phones.get(index).setIsMain(true);
    }

    public void clearPhones() {
        this.phones.clear();
    }

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}