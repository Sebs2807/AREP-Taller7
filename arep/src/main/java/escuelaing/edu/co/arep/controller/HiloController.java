package escuelaing.edu.co.arep.controller;

import org.springframework.web.bind.annotation.*;
import escuelaing.edu.co.arep.model.Hilo;
import escuelaing.edu.co.arep.repository.HiloRepository;
import escuelaing.edu.co.arep.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

@RestController
@RequestMapping("/hilos")
@CrossOrigin(origins = "https://minitwitter-camilo.s3.amazonaws.com")
@Tag(name = "Hilos", description = "Operaciones para gestionar hilos (streams)")
public class HiloController {

    private final HiloRepository hiloRepo;
    private final UsuarioRepository usuarioRepo;

    public HiloController(HiloRepository hiloRepo, UsuarioRepository usuarioRepo) {
        this.hiloRepo = hiloRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @PostMapping
    @Operation(summary = "Crear hilo", description = "Crea un nuevo hilo. Se requiere autenticación; el usuario autenticado será el owner.")
    public Hilo create(@RequestBody Hilo body, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) throw new RuntimeException("No autenticado");

   
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("cognito:username");
        String displayName = jwt.getClaimAsString("name");

        body.setOwnerEmail(email);

      
        var ownerOpt = usuarioRepo.findByEmail(email);
        var owner = ownerOpt.orElseGet(() -> {
            var nuevo = new escuelaing.edu.co.arep.model.Usuario();
            nuevo.setEmail(email);
            nuevo.setUsername(username != null ? username : email);
            nuevo.setDisplayName(displayName != null ? displayName : username);
            return usuarioRepo.save(nuevo);
        });
        body.setOwner(owner);
        return hiloRepo.save(body);
    }

    @GetMapping
    @Operation(summary = "Listar hilos", description = "Devuelve todos los hilos (streams)")
    public List<Hilo> getAll() {
        return hiloRepo.findAll();
    }
}
