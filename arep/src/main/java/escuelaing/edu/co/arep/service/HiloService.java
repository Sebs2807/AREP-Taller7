package escuelaing.edu.co.arep.service;

import escuelaing.edu.co.arep.model.Hilo;
import escuelaing.edu.co.arep.model.Usuario;
import escuelaing.edu.co.arep.repository.HiloRepository;
import escuelaing.edu.co.arep.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class HiloService {

    private final HiloRepository hiloRepo;
    private final UsuarioRepository usuarioRepo;

    public HiloService(HiloRepository hiloRepo, UsuarioRepository usuarioRepo) {
        this.hiloRepo = hiloRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public Hilo create(String title, String ownerId) {
        Usuario owner = usuarioRepo.findById(ownerId).orElseThrow();
        Hilo hilo = new Hilo();
        hilo.setId("hilo_" + UUID.randomUUID());
        hilo.setTitle(title);
        hilo.setOwner(owner);
        return hiloRepo.save(hilo);
    }

    public List<Hilo> getAll() {
        return hiloRepo.findAll();
    }
}
