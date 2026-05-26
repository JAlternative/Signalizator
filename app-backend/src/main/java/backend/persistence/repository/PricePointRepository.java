package backend.persistence.repository;

import backend.persistence.entity.PricePoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricePointRepository extends JpaRepository<PricePoint, Long> {
}