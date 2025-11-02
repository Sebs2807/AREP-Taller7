package edu.eci.arep.post_service.controller;

import edu.eci.arep.post_service.model.Post;
import edu.eci.arep.post_service.model.Usuario;
import edu.eci.arep.post_service.repository.UsuarioRepository;
import edu.eci.arep.post_service.service.PostService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/hilos")
@CrossOrigin(origins = "https://minitwitter-camilo.s3.amazonaws.com")
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
    public Post createPost(@PathVariable String hiloId,
                           @RequestBody PostRequest postRequest,
                           @AuthenticationPrincipal Jwt jwt) {

        if (jwt == null) throw new RuntimeException("No autenticado");

        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("cognito:username");
        String displayName = jwt.getClaimAsString("name");


        var ownerOpt = usuarioRepo.findByEmail(email);
        var owner = ownerOpt.orElseGet(() -> {
            var nuevo = new Usuario();
            nuevo.setEmail(email);
            nuevo.setUsername(username != null ? username : email);
            nuevo.setDisplayName(displayName != null ? displayName : username);
            return usuarioRepo.save(nuevo);
        });

        return postService.createPost(hiloId, owner.getId(), postRequest.content);
    }

    @GetMapping("/{hiloId}/posts")
    public List<Post> getPostsByHilo(@PathVariable String hiloId) {
        return postService.getPostsByHilo(hiloId);
    }
}
