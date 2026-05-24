package backend.integration.moex;

import backend.integration.moex.dto.MoexMarketDataSnapshot;
import backend.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MoexMarketDataParser {

    public Optional<MoexMarketDataSnapshot> parseMarketDataSnapshot(String rawJson) {
        return Utils.readTree(rawJson, MoexMarketDataSnapshot.class);
    }
}
