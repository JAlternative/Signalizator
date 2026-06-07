package backend.persistence.repository;

import backend.persistence.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RuleRepository extends JpaRepository<Rule, Long> {

    Optional<Rule> findByCode(String code);

    List<Rule> findByEnabledTrue();
}
