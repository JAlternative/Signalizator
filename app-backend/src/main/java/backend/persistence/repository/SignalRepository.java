package backend.persistence.repository;

import backend.persistence.entity.Signal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignalRepository extends JpaRepository<Signal, Long> {

    boolean existsByDedupKey(String dedupKey);

    List<Signal> findTop100ByStatusOrderByCreatedAtDesc(String status);
}
