package backend.persistence.repository;

import backend.persistence.entity.Instrument;
import backend.persistence.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    List<Instrument> findByEnabledTrue();
    Optional<Instrument> findBySourceAndTickerAndBoard(Source source, String ticker, String board);
    List<Instrument> findAllBySourceId(Long sourceId);

}
