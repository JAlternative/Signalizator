package backend.persistence.repository;

import backend.persistence.entity.PricePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricePointRepository extends JpaRepository<PricePoint, Long> {

    List<PricePoint> findTop100ByInstrumentIdOrderBySystemTimeDesc(Long instrumentId);
}