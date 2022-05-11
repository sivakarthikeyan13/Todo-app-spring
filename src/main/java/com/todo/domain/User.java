package com.todo.domain;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(name="user_sequence", sequenceName = "user_sequence", initialValue = 1000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_sequence")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Transient
    private String newPassword;
    @Transient
    private String confirmPassword;

    public User() {
    }

    public User(Long id, String name, String email, String password, String newPassword, String confirmPassword) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public User(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, String newPassword, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name.trim();
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email.trim();
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password.trim();
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        newPassword = newPassword.trim();
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        confirmPassword = confirmPassword.trim();
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", passwordConfirm='" + confirmPassword + '\'' +
                '}';
    }
}

