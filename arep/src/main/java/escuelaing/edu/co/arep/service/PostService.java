package escuelaing.edu.co.arep.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import escuelaing.edu.co.arep.model.*;
import escuelaing.edu.co.arep.repository.*;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

	private final PostRepository postRepo;
	private final HiloRepository hiloRepo;
	private final UsuarioRepository usuarioRepo;

	public PostService(PostRepository postRepo, HiloRepository hiloRepo, UsuarioRepository usuarioRepo) {
		this.postRepo = postRepo;
		this.hiloRepo = hiloRepo;
		this.usuarioRepo = usuarioRepo;
	}

	public Post createPost(String hiloId, String userId, String content) {
		if (content == null || content.isBlank()) {
			throw new IllegalArgumentException("El contenido del post no puede estar vacío.");
		}
		if (content.length() > 140) {
			throw new IllegalArgumentException("El post debe tener máximo 140 caracteres.");
		}

		Optional<Hilo> optionalHilo = hiloRepo.findById(hiloId);
		if (optionalHilo.isEmpty()) {
			throw new RuntimeException("Hilo no encontrado con id: " + hiloId);
		}
		Hilo hilo = optionalHilo.get();

		Optional<Usuario> optionalUsuario = usuarioRepo.findById(userId);
		if (optionalUsuario.isEmpty()) {
			throw new RuntimeException("Usuario no encontrado con id: " + userId);
		}
		Usuario author = optionalUsuario.get();

		Post post = new Post();
		post.setContent(content);
		post.setHilo(hilo);
		post.setAuthor(author);

		return postRepo.save(post);
	}

	public List<Post> getPostsByHilo(String hiloId) {
		Optional<Hilo> optionalHilo = hiloRepo.findById(hiloId);
		if (optionalHilo.isEmpty()) {
			throw new RuntimeException("Hilo no encontrado con id: " + hiloId);
		}
		Hilo hilo = optionalHilo.get();

		return postRepo.findByHiloOrderByCreatedAtDesc(hilo);
	}
}
