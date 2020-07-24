package com.damian.dto;

import com.damian.domain.user.Authority;
import com.damian.domain.user.User;

import java.util.Set;

public class UserDto {

    private Long id;
    private String login;
    private String password;
    private boolean activated = false;
    private Set<Authority> authorities;

    public UserDto() {
    }

    public UserDto(User user) {
        this(user.getId(), user.getLogin(), user.getPassword(), user.isActivated(), user.getAuthorities());
    }

    public UserDto(Long id, String login, String password, boolean activated, Set<Authority> authorities) {
        this.id = id;
        this.login = login;
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
}
