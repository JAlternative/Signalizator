package backend.service;

import backend.persistence.entity.PricePoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MoexMarketDataServiceTest {

    @Autowired
    private MoexMarketDataService moexMarketDataService;


    @Test
    void testMoexMarketDataService() {

        Optional<PricePoint> pricePoint = moexMarketDataService.collectAndSaveSnapshot(1L);
        System.out.println(pricePoint);
        List<PricePoint> pointList = moexMarketDataService.getPricePointHistory(1L);
        pricePoint.get();





    }


}