package edu.eci.arep.post_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario {
    @Id
    private String id = "usuario_" + UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Usuario() {

    }

    private String email;
    private String displayName;
    private Instant createdAt = Instant.now();
}
