package edu.eci.arep.post_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Hilo {
    @Id
    private String id = "hilo_" + UUID.randomUUID();

    private String title;

    @ManyToOne(optional = false)
    private Usuario owner;

    private Instant createdAt = Instant.now();

    private String ownerEmail;

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Usuario getOwner() {
        return owner;
    }

    public void setOwner(Usuario owner) {
        this.owner = owner;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}