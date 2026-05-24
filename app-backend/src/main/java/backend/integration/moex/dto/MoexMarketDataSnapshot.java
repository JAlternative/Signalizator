package backend.integration.moex.dto;

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
        String secId,
        String boardId,
        BigDecimal bid,
        BigDecimal offer,
        BigDecimal spread,
        BigDecimal open,
        BigDecimal low,
        BigDecimal high,
        BigDecimal last,
        BigDecimal waprice,
        BigDecimal change,
        Integer numTrades,
        Long volumeToday,
        Long valueToday,
        String tradingStatus,
        String updateTime,
        String systemTime
) {
}
