package escuelaing.edu.co.arep.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
public class Usuario {
	@Id
	private String id = "usuario_" + UUID.randomUUID();

	@Column(unique = true, nullable = false)
	private String username;

	@JsonIgnore
	private String password;

	private String email;
	private String displayName;
	private Instant createdAt = Instant.now();
}
