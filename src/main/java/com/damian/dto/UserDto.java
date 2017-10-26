package com.damian.dto;

import com.damian.model.Authority;
import com.damian.model.User;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;


public class UserDto {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean activated = false;
    private Set<Authority> authorities;

    public UserDto() {
        // Empty constructor needed for Jackson.
    }

    public UserDto(User user) {
        this(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(),
                user.getEmail(),user.getPassword(), user.getActivated(),
                user.getAuthorities());
    }

    public UserDto(Long id, String login, String firstName, String lastName,
                   String email,String password, boolean activated,
                   Set<Authority> authorities) {

        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.activated = activated;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", activated=" + activated +
                ", authorities=" + authorities +
                '}';
    }
}
