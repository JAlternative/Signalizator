package backend.service;

import backend.persistence.entity.Signal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceChangeSignalServiceTest {

    @Autowired
    private PriceChangeSignalService priceChangeSignalService;

    @Test
    void testPriceChangeSignalService() {
        Optional<Signal> signal = priceChangeSignalService.generateForInstrument(1L);
        System.out.println(signal);
    }
}