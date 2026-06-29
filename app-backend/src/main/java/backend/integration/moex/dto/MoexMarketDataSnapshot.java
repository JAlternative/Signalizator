package backend.integration.moex.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Snapshot рыночных данных по инструменту MOEX")
public record MoexMarketDataSnapshot(

        @JsonAlias("SECID")
        @Schema(description = "SECID — тикер инструмента на MOEX. Например: SBER, GAZP, LKOH.")
        String secId,

        @JsonAlias("BOARDID")
        @Schema(description = "BOARDID — режим торгов. Например: TQBR — основной режим торгов российскими акциями.")
        String boardId,

        @JsonAlias("BID")
        @Schema(description = "BID — лучшая цена покупки. Максимальная цена, по которой покупатели готовы купить инструмент.")
        BigDecimal bid,

        @JsonAlias("OFFER")
        @Schema(description = "OFFER — лучшая цена продажи. Минимальная цена, по которой продавцы готовы продать инструмент.")
        BigDecimal offer,

        @JsonAlias("SPREAD")
        @Schema(description = "SPREAD — разница между OFFER и BID. Показывает разрыв между лучшей продажей и лучшей покупкой.")
        BigDecimal spread,

        @JsonAlias("OPEN")
        @Schema(description = "OPEN — цена открытия торгового дня.")
        BigDecimal open,

        @JsonAlias("LOW")
        @Schema(description = "LOW — минимальная цена инструмента за текущий торговый день.")
        BigDecimal low,

        @JsonAlias("HIGH")
        @Schema(description = "HIGH — максимальная цена инструмента за текущий торговый день.")
        BigDecimal high,

        @JsonAlias("LAST")
        @Schema(description = "LAST — последняя цена сделки. Основное поле, по которому дальше строится история price_points.")
        BigDecimal last,

        @JsonAlias("WAPRICE")
        @Schema(description = "WAPRICE — средневзвешенная цена сделок с учётом объёма.")
        BigDecimal waprice,

        @JsonAlias("CHANGE")
        @Schema(description = "CHANGE — изменение цены инструмента относительно предыдущего значения или закрытия.")
        BigDecimal change,

        @JsonAlias("NUMTRADES")
        @Schema(description = "NUMTRADES — количество сделок за текущий торговый день.")
        Integer numTrades,

        @JsonAlias("VOLTODAY")
        @Schema(description = "VOLTODAY — объём торгов за день в штуках. Сколько бумаг проторговали.")
        Long volumeToday,

        @JsonAlias("VALTODAY")
        @Schema(description = "VALTODAY — оборот торгов за день в деньгах. На какую сумму проторговали инструмент.")
        Long valueToday,

        @JsonAlias("TRADINGSTATUS")
        @Schema(description = "TRADINGSTATUS — статус торгов инструмента. Например, торги идут, остановлены или инструмент не торгуется.")
        String tradingStatus,

        @JsonAlias("UPDATETIME")
        @Schema(description = "UPDATETIME — время последнего обновления данных на стороне MOEX.")
        String updateTime,

        @JsonAlias("SYSTIME")
        @Schema(description = "SYSTIME — системное время snapshot от MOEX. Потом сохраняется в PricePoint.systemTime.")
        String systemTime
) {
}