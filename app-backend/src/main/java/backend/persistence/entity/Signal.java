package backend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "signals")
@Getter
@Setter
@NoArgsConstructor
public class Signal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private Rule rule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_point_id")
    private PricePoint pricePoint;

    @Column(name = "type", nullable = false, length = 64)
    private String type;

    @Column(name = "severity", nullable = false, length = 32)
    private String severity;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "explanation", nullable = false, columnDefinition = "text")
    private String explanation;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "facts_json", columnDefinition = "jsonb")
    private Map<String, Object> factsJson;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "NEW";

    @Column(name = "dedup_key", nullable = false, length = 255)
    private String dedupKey;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}