package edu.eci.arep.hilo_service.repository;

import edu.eci.arep.hilo_service.model.Hilo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HiloRepository extends JpaRepository<Hilo, String> {
}
