package backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MoexWatchlistCollectorServiceTest {

    @Autowired
    private MoexWatchlistCollectorService moexWatchlistCollectorService;


    @Test
    void testMoexWatchlistCollectorService() {
       List<Long> list = moexWatchlistCollectorService.collectEnabledMoexWatchlistOnce();
       System.out.println(list);
    }

}