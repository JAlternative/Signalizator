package backend.integration.moex.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

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
        @JsonAlias("SECID")
        String secId,

        @JsonAlias("BOARDID")
        String boardId,

        @JsonAlias("BID")
        BigDecimal bid,

        @JsonAlias("OFFER")
        BigDecimal offer,

        @JsonAlias("SPREAD")
        BigDecimal spread,

        @JsonAlias("OPEN")
        BigDecimal open,

        @JsonAlias("LOW")
        BigDecimal low,

        @JsonAlias("HIGH")
        BigDecimal high,

        @JsonAlias("LAST")
        BigDecimal last,

        @JsonAlias("WAPRICE")
        BigDecimal waprice,

        @JsonAlias("CHANGE")
        BigDecimal change,

        @JsonAlias("NUMTRADES")
        Integer numTrades,

        @JsonAlias("VOLTODAY")
        Long volumeToday,

        @JsonAlias("VALTODAY")
        Long valueToday,

        @JsonAlias("TRADINGSTATUS")
        String tradingStatus,

        @JsonAlias("UPDATETIME")
        String updateTime,

        @JsonAlias("SYSTIME")
        String systemTime
) {
}
