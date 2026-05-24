package backend.integration.moex;

import backend.integration.moex.dto.MoexMarketDataSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MoexMarketDataParser {

    private final MoexTableMapper moexTableMapper;

    public Optional<MoexMarketDataSnapshot> parseMarketDataSnapshot(String rawJson) {
        return moexTableMapper.mapFirstRow(rawJson, "marketdata", MoexMarketDataSnapshot.class);
    }
}