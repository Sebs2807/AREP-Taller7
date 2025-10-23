package escuelaing.edu.co.arep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import escuelaing.edu.co.arep.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
	Usuario findByUsername(String username);
}
