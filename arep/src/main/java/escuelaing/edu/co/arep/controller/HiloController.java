package escuelaing.edu.co.arep.controller;

import org.springframework.web.bind.annotation.*;
import escuelaing.edu.co.arep.model.Hilo;
import escuelaing.edu.co.arep.repository.HiloRepository;
import escuelaing.edu.co.arep.repository.UsuarioRepository;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/hilos")
@CrossOrigin(origins = "*")
@Tag(name = "Hilos", description = "Operaciones para gestionar hilos (streams)")
public class HiloController {

    private final HiloRepository hiloRepo;
    private final UsuarioRepository usuarioRepo;

    public HiloController(HiloRepository hiloRepo, UsuarioRepository usuarioRepo) {
        this.hiloRepo = hiloRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @PostMapping
    @Operation(summary = "Crear hilo", description = "Crea un nuevo hilo; enviar objeto Hilo con owner.id")
    public Hilo create(@RequestBody Hilo body) {
        var owner = usuarioRepo.findById(body.getOwner().getId()).orElseThrow();
        body.setOwner(owner);
        return hiloRepo.save(body);
    }

    @GetMapping
    @Operation(summary = "Listar hilos", description = "Devuelve todos los hilos (streams)")
    public List<Hilo> getAll() {
        return hiloRepo.findAll();
    }
}
