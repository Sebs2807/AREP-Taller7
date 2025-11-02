package edu.eci.arep.post_service.repository;

import edu.eci.arep.post_service.model.Hilo;
import edu.eci.arep.post_service.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByHiloOrderByCreatedAtDesc(Hilo hilo);
}
