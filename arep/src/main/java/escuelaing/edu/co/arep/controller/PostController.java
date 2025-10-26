package escuelaing.edu.co.arep.controller;

import org.springframework.web.bind.annotation.*;
import escuelaing.edu.co.arep.model.Post;
import escuelaing.edu.co.arep.service.PostService;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("hilos")
@CrossOrigin(origins = "*")
@Tag(name = "Posts", description = "Operaciones para crear y listar posts dentro de hilos")
public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/{hiloId}/posts")
	@Operation(summary = "Crear post", description = "Crear un post en el hilo indicado. Se requiere autenticación; el usuario autenticado será el autor.")
	public Post createPost(@Parameter(description = "ID del hilo") @PathVariable String hiloId,
						   @Parameter(description = "Contenido del post (máx 140 caracteres)") @RequestParam String content) {
		// obtener usuario autenticado desde el contexto de seguridad
		var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getName() == null) {
			throw new RuntimeException("No autenticado");
		}
		String userId = auth.getName();
		return postService.createPost(hiloId, userId, content);
	}

	@GetMapping("/{hiloId}/posts")
	@Operation(summary = "Listar posts", description = "Devuelve los posts de un hilo ordenados por fecha descendente")
	public List<Post> getPostsByHilo(@Parameter(description = "ID del hilo") @PathVariable String hiloId) {
		return postService.getPostsByHilo(hiloId);
	}
}
