package edu.eci.arep.post_service.repository;

import edu.eci.arep.post_service.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Usuario findByUsername(String username);
    java.util.Optional<Usuario> findByEmail(String email);
}

