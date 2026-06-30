package backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignalEngineService {

    private final PriceChangeSignalService priceChangeSignalService;
    private final VolumeSpikeSignalService volumeSpikeSignalService;

    /**
     * сохранили новую цену
     * → проверили изменение цены
     * → проверили всплеск объёма
     * → если что-то сработало, создали signal
     * */
    public void analyzeInstrument(Long instrumentId) {
        priceChangeSignalService.generateForInstrument(instrumentId);
        volumeSpikeSignalService.generateForInstrument(instrumentId);
    }
}
