package com.damian.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @Column(name = "name", length = 100, nullable = true)
    private String name;

    @Column(name = "email", length = 100, unique = true, nullable = true)
    private String email;

    @JsonIgnore
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60)
    private String password;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_authority", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<Authority>();

    @Basic
    @Column(name = "is_archival", columnDefinition = "boolean default false")
    private Boolean isArchival;

    @Basic
    @Column(name = "order_total_amount")
    private Integer points;

    @Basic
    @Column(name = "first_login", columnDefinition = "boolean default false")
    private boolean ifFirstLogin;


    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Boolean getArchival() {
        return isArchival;
    }

    public void setArchival(Boolean archival) {
        isArchival = archival;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIfFirstLogin() {
        return ifFirstLogin;
    }

    public void setIfFirstLogin(boolean ifFirstLogin) {
        this.ifFirstLogin = ifFirstLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return activated == user.activated && Objects.equals(id, user.id) && Objects.equals(login, user.login)
            && Objects.equals(password, user.password) && Objects.equals(authorities, user.authorities)
            && Objects.equals(isArchival, user.isArchival) && Objects.equals(points, user.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, activated, authorities, isArchival, points);
    }


}
