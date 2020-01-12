package com.damian.dto;

import com.damian.domain.user.Authority;
import com.damian.domain.user.User;
import java.util.Set;

public class ProductionUserDto {

    private Long id;
    private String login;
    private Set<Authority> authorities;

    public ProductionUserDto() {
    }

    public ProductionUserDto(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    public ProductionUserDto(Long id, String login, Set<Authority> authorities) {
        this.id = id;
        this.login = login;
        this.authorities = authorities;
    }

    public ProductionUserDto(User user) {
        this(user.getId(), user.getLogin(), user.getAuthorities());
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

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}


