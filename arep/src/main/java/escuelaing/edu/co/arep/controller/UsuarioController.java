package escuelaing.edu.co.arep.controller;

import org.springframework.web.bind.annotation.*;
import escuelaing.edu.co.arep.model.Usuario;
import escuelaing.edu.co.arep.repository.UsuarioRepository;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://minitwitter-camilo.s3-website-us-east-1.amazonaws.com")
@Tag(name = "Usuarios", description = "Operaciones para gestionar usuarios")
public class UsuarioController {
	private final UsuarioRepository usuarioRepo;

	public UsuarioController(UsuarioRepository usuarioRepo) {
		this.usuarioRepo = usuarioRepo;
	}

	@GetMapping
	@Operation(summary = "Listar usuarios", description = "Devuelve la lista de usuarios registrados")
	public List<Usuario> getAll() {
		return usuarioRepo.findAll();
	}

	@PostMapping
	@Operation(summary = "Crear usuario", description = "Crea un nuevo usuario con username único")
	public Usuario create(@RequestBody Usuario usuario) {
		return usuarioRepo.save(usuario);
	}
}
