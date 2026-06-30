package backend.integration.moex;

import backend.integration.moex.dto.MoexMarketDataSnapshot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * Что мы сейчас запрашиваем у MOEX ISS:
 *
 * baseUrl = https://iss.moex.com
 * Адрес API Московской биржи.
 *
 * market = shares
 * Рынок акций. Сейчас в проекте используем именно shares.
 * Другие возможные рынки: bonds — облигации, index — индексы,
 * foreignshares — иностранные акции. Пока они в проекте не используются.
 *
 * board = TQBR
 * Режим торгов. TQBR — основной режим торгов российскими акциями.
 * Для других инструментов бывают другие board, например:
 * TQOB / TQCB — облигации,
 * TQTF — ETF / фонды.
 *
 * ticker = SBER
 * Конкретный инструмент. Здесь это акция Сбербанка.
 * Сейчас в проекте заведены SBER, GAZP, LKOH.
 *
 * Итого текущая схема проекта:
 * MOEX -> stock -> shares -> TQBR -> конкретный тикер.
 */

@SpringBootTest
class MoexIssClientTest {

    @Autowired
    private MoexIssClient moexIssClient;

    @Autowired
    private MoexMarketDataParser moexMarketDataParser;

    @Test
    void testPingMoex() {

       String result = moexIssClient.getSecurityRawJson(
               "https://iss.moex.com",
               "shares",
               "TQBR",
               "SBER");

       Assertions.assertTrue(result.contains("Сбербанк"));

       MoexMarketDataSnapshot snapshot =  moexMarketDataParser.parseMarketDataSnapshot(result).orElse(null);
       Assertions.assertNotNull(snapshot);
       Assertions.assertEquals("SBER", snapshot.secId());

       System.out.println();




    }


}