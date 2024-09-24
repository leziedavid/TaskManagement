package com.mobisoft.taskmanagement.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "users")
@Data
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String lastname;
    private String firstname;
    private String phone;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private String fonction;
    private String token;
    private String profil;
    private int otp;

    @Column(name = "is_valides", columnDefinition = "integer default 0")
    private Integer isValides = 0;

    @Enumerated(EnumType.STRING)
    private Gender genre;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // Valeur par défaut pour le rôle
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime usersCreatedAt = OffsetDateTime.now();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime usersUpdatedAt = OffsetDateTime.now();

    // @ManyToMany(mappedBy = "users")
    // private Set<Department> departments;


    @OneToMany
    @JoinTable(
        name = "department_users",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )

    @JsonIgnore
    private List<Department> departments = new ArrayList<>();

    public List<Department> getDepartments() {
        return departments;
    }

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    // @OneToMany(mappedBy = "user")
    // @JsonIgnore
    // private List<Leave> leaves = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Leave> leaves = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(fonction));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return String return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return String return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return String return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String return the fonction
     */
    public String getFonction() {
        return fonction;
    }

    /**
     * @param fonction the fonction to set
     */
    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    /**
     * @return int return the otp
     */
    public int getOtp() {
        return otp;
    }

    /**
     * @param otp the otp to set
     */
    public void setOtp(int otp) {
        this.otp = otp;
    }

    /**
     * @return Gender return the genre
     */
    public Gender getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(Gender genre) {
        this.genre = genre;
    }


    // Getters et setters pour le champ role
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    /**
     * @return OffsetDateTime return the usersCreatedAt
     */
    public OffsetDateTime getUsersCreatedAt() {
        return usersCreatedAt;
    }

    /**
     * @param usersCreatedAt the usersCreatedAt to set
     */
    public void setUsersCreatedAt(OffsetDateTime usersCreatedAt) {
        this.usersCreatedAt = usersCreatedAt;
    }

    /**
     * @return OffsetDateTime return the usersUpdatedAt
     */
    public OffsetDateTime getUsersUpdatedAt() {
        return usersUpdatedAt;
    }

    /**
     * @param usersUpdatedAt the usersUpdatedAt to set
     */
    public void setUsersUpdatedAt(OffsetDateTime usersUpdatedAt) {
        this.usersUpdatedAt = usersUpdatedAt;
    }

}
