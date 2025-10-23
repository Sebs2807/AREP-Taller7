package escuelaing.edu.co.arep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import escuelaing.edu.co.arep.model.Hilo;

public interface HiloRepository extends JpaRepository<Hilo, String> {
}
