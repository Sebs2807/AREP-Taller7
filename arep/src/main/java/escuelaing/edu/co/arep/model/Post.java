package escuelaing.edu.co.arep.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;

@Entity
public class Post {
	@Id
	private String id = "post_" + UUID.randomUUID();

	@ManyToOne(optional = false)
	private Hilo hilo;

	@ManyToOne(optional = false)
	private Usuario author;

	@Column(length = 140)
	private String content;

	private Instant createdAt = Instant.now();

	public void setContent(String content2) {
		this.content = content2;
	}

    public void setHilo(Hilo hilo2) {
		this.hilo = hilo2;
    }

    public void setAuthor(Usuario author2) {
        this.author = author2;
    }

	public String getId() {
		return id;
	}

	public Hilo getHilo() {
		return hilo;
	}

	public Usuario getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}