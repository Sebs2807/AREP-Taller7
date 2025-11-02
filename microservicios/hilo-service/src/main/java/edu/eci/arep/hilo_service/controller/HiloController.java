package edu.eci.arep.hilo_service.controller;

import edu.eci.arep.hilo_service.model.Hilo;
import edu.eci.arep.hilo_service.model.Usuario;
import edu.eci.arep.hilo_service.repository.HiloRepository;
import edu.eci.arep.hilo_service.repository.UsuarioRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

@RestController
@RequestMapping("/hilos")
@CrossOrigin(origins = "https://minitwitter-camilo.s3.amazonaws.com")
public class HiloController {

    private final HiloRepository hiloRepo;
    private final UsuarioRepository usuarioRepo;

    public HiloController(HiloRepository hiloRepo, UsuarioRepository usuarioRepo) {
        this.hiloRepo = hiloRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @PostMapping
    public Hilo create(@RequestBody Hilo body, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) throw new RuntimeException("No autenticado");


        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("cognito:username");
        String displayName = jwt.getClaimAsString("name");

        body.setOwnerEmail(email);


        var ownerOpt = usuarioRepo.findByEmail(email);
        var owner = ownerOpt.orElseGet(() -> {
            var nuevo = new Usuario();
            nuevo.setEmail(email);
            nuevo.setUsername(username != null ? username : email);
            nuevo.setDisplayName(displayName != null ? displayName : username);
            return usuarioRepo.save(nuevo);
        });
        body.setOwner(owner);
        return hiloRepo.save(body);
    }

    @GetMapping
    public List<Hilo> getAll() {
        return hiloRepo.findAll();
    }
}

