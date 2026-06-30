package backend.service;

import backend.api.response.SignalResponse;
import backend.persistence.entity.Signal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignalReadServiceTest {

    @Autowired
    private SignalReadService signalReadService;

    @Autowired
    private SignalCommandService signalCommandService;

    @Test
    void testSignalReadService() {
        List<SignalResponse> signalResponses = signalReadService.getLatestNewSignals();
        System.out.println(signalResponses);
        Optional<Signal> signal = signalCommandService.ack(14L);
        System.out.println(signal);
        List<SignalResponse> signalResponses2 = signalReadService.getLatestNewSignals();
        System.out.println(signalResponses2);
    }
}