package backend.integration.moex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
/**
 * secId / boardId       = что за инструмент и режим торгов
 * bid / offer / spread  = стакан: лучшая покупка, лучшая продажа, разница
 * open / low / high     = цена открытия, минимум, максимум
 * last                  = последняя цена
 * waprice               = средневзвешенная цена
 * change                = изменение цены
 * numTrades             = количество сделок
 * volumeToday           = объём в штуках
 * valueToday            = оборот в деньгах
 * tradingStatus         = статус торгов
 * updateTime/systemTime = время обновления данных
 * */
public record MoexMarketDataSnapshot(
        @JsonProperty("SECID")
        String secId,

        @JsonProperty("BOARDID")
        String boardId,

        @JsonProperty("BID")
        BigDecimal bid,

        @JsonProperty("OFFER")
        BigDecimal offer,

        @JsonProperty("SPREAD")
        BigDecimal spread,

        @JsonProperty("OPEN")
        BigDecimal open,

        @JsonProperty("LOW")
        BigDecimal low,

        @JsonProperty("HIGH")
        BigDecimal high,

        @JsonProperty("LAST")
        BigDecimal last,

        @JsonProperty("WAPRICE")
        BigDecimal waprice,

        @JsonProperty("CHANGE")
        BigDecimal change,

        @JsonProperty("NUMTRADES")
        Integer numTrades,

        @JsonProperty("VOLTODAY")
        Long volumeToday,

        @JsonProperty("VALTODAY")
        Long valueToday,

        @JsonProperty("TRADINGSTATUS")
        String tradingStatus,

        @JsonProperty("UPDATETIME")
        String updateTime,

        @JsonProperty("SYSTIME")
        String systemTime
) {
}
