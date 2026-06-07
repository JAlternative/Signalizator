package backend.persistence.repository;

import backend.persistence.entity.Signal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignalRepository extends JpaRepository<Signal, Long> {
}
