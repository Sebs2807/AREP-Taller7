package escuelaing.edu.co.arep.service;

import escuelaing.edu.co.arep.model.Usuario;
import escuelaing.edu.co.arep.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public List<Usuario> getAll() {
        return usuarioRepo.findAll();
    }

    public Usuario create(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public Usuario findById(String id) {
        return usuarioRepo.findById(id).orElseThrow();
    }
}
