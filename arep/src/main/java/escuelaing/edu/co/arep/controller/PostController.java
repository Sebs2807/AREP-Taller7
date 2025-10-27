package escuelaing.edu.co.arep.controller;

import org.springframework.web.bind.annotation.*;
import escuelaing.edu.co.arep.model.Post;
import escuelaing.edu.co.arep.service.PostService;
import escuelaing.edu.co.arep.repository.UsuarioRepository;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/hilos")
@CrossOrigin(origins = "http://minitwitter-camilo.s3-website-us-east-1.amazonaws.com")
@Tag(name = "Posts", description = "Operaciones para crear y listar posts dentro de hilos")
public class PostController {

    private final PostService postService;
    private final UsuarioRepository usuarioRepo;

    public PostController(PostService postService, UsuarioRepository usuarioRepo) {
        this.postService = postService;
        this.usuarioRepo = usuarioRepo;
    }

    public static class PostRequest {
        public String content;
    }

    @PostMapping("/{hiloId}/posts")
    @Operation(summary = "Crear post", description = "Crear un post en el hilo indicado. Se requiere autenticación; el usuario autenticado será el autor.")
    public Post createPost(@Parameter(description = "ID del hilo") @PathVariable String hiloId,
                           @RequestBody PostRequest postRequest,
                           @AuthenticationPrincipal Jwt jwt) {

        if (jwt == null) throw new RuntimeException("No autenticado");

        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("cognito:username");
        String displayName = jwt.getClaimAsString("name");

     
        var ownerOpt = usuarioRepo.findByEmail(email);
        var owner = ownerOpt.orElseGet(() -> {
            var nuevo = new escuelaing.edu.co.arep.model.Usuario();
            nuevo.setEmail(email);
            nuevo.setUsername(username != null ? username : email);
            nuevo.setDisplayName(displayName != null ? displayName : username);
            return usuarioRepo.save(nuevo);
        });

        return postService.createPost(hiloId, owner.getId(), postRequest.content);
    }

    @GetMapping("/{hiloId}/posts")
    @Operation(summary = "Listar posts", description = "Devuelve los posts de un hilo ordenados por fecha descendente")
    public List<Post> getPostsByHilo(@Parameter(description = "ID del hilo") @PathVariable String hiloId) {
        return postService.getPostsByHilo(hiloId);
    }
}
