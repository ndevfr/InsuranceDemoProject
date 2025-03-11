package fr.ndev.insurance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="main_phone")
    private boolean isMain;

    @ManyToOne @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Phone() {}

    public Phone(Long id, String phoneNumber, boolean isMain, User user) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.isMain = isMain;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(boolean mainPhone) {
        this.isMain = mainPhone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}