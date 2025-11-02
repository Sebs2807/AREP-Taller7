package edu.eci.arep.usuario_service.controller;


import edu.eci.arep.usuario_service.model.Usuario;
import edu.eci.arep.usuario_service.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://minitwitter-camilo.s3-website-us-east-1.amazonaws.com")
public class UsuarioController {
    private final UsuarioRepository usuarioRepo;

    public UsuarioController(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioRepo.findAll();
    }

    @PostMapping
    public Usuario create(@RequestBody Usuario usuario) {
        return usuarioRepo.save(usuario);
    }
}
