package escuelaing.edu.co.arep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import escuelaing.edu.co.arep.model.Post;
import escuelaing.edu.co.arep.model.Hilo;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByHiloOrderByCreatedAtDesc(Hilo hilo);
}
