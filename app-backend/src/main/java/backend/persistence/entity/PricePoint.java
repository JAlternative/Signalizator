package backend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "price_points")
@NoArgsConstructor
public class PricePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(name = "sec_id", nullable = false, length = 64)
    private String secId;

    @Column(name = "board_id", nullable = false, length = 32)
    private String boardId;

    @Column(name = "bid", precision = 19, scale = 6)
    private BigDecimal bid;

    @Column(name = "offer", precision = 19, scale = 6)
    private BigDecimal offer;

    @Column(name = "spread", precision = 19, scale = 6)
    private BigDecimal spread;

    @Column(name = "open_price", precision = 19, scale = 6)
    private BigDecimal openPrice;

    @Column(name = "low_price", precision = 19, scale = 6)
    private BigDecimal lowPrice;

    @Column(name = "high_price", precision = 19, scale = 6)
    private BigDecimal highPrice;

    @Column(name = "last_price", precision = 19, scale = 6)
    private BigDecimal lastPrice;

    @Column(name = "waprice", precision = 19, scale = 6)
    private BigDecimal waprice;

    @Column(name = "change", precision = 19, scale = 6)
    private BigDecimal change;

    @Column(name = "num_trades")
    private Integer numTrades;

    @Column(name = "volume_today")
    private Long volumeToday;

    @Column(name = "value_today")
    private Long valueToday;

    @Column(name = "trading_status", length = 16)
    private String tradingStatus;

    @Column(name = "update_time")
    private LocalTime updateTime;

    @Column(name = "system_time")
    private LocalDateTime systemTime;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

}